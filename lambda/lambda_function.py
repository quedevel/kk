
import sys
import logging
import pymysql
import urllib.request
import requests
import re
import json
from bs4 import BeautifulSoup

def lambda_handler(event, context):
    conn = pymysql.connect(host='jarvis1.clzkjwwf2qt9.ap-northeast-2.rds.amazonaws.com', user='admin', passwd='rlxhdqks001!', db='jarvis')
    curs = conn.cursor()
    
    selectLastData = 'select title from tbl_community_crawling where last_crawling = 1 order by no desc limit 1'
    
    addSpecialPrice = 'insert into tbl_community_crawling (category, title, price, fee, image, link, last_crawling) values(%s, %s, %s, %s, %s, %s, %s)'
    
    curs.execute(selectLastData)
    
    lastData = curs.fetchone()
    
    if lastData != None:
        lastData = lastData[0]
    
    url = "http://www.ppomppu.co.kr/zboard/zboard.php?id=ppomppu&page=1&divpage=58"
    soup = CrawlingUtil.crawl(url)

    items = soup.select('.list0, .list1')

    last_crawling = True
    
    parentheses = re.compile('\(.+?\)')
    squareBrackets = re.compile('\[.+?\]')
    priceSplit = re.compile('[ /]')
    priceParsing = re.compile('[^0-9/ ]+')

    for item in items:
        titleTag = item.select_one('.list_title')
        if titleTag:
            titleText = titleTag.text
            title = parentheses.sub('', squareBrackets.sub('', titleText)).strip()
           
            if title == lastData:
                break
 
            # priceTag = parentheses.findall(titleText)
            priceTag = squareBrackets.findall(titleText)
            priceTag.extend(parentheses.findall(titleText))
            
            boardLink = 'http://www.ppomppu.co.kr/zboard/' + item.select('a')[1].get('href')

            link = CrawlingUtil.crawl(boardLink).select_one('.wordfix a').get('href')
            category = item.select_one('nobr.list_vspace').text
            price = 0
            fee = 0
            
            if priceTag:
                priceArr = []
                for num in priceTag:
                    priceArr.extend(priceSplit.split(priceParsing.sub('', num).strip()))

                priceArr = list(map(lambda i: 0 if i == '' else int(i), priceArr))
                
                if len(priceArr) > 1:
                    fee = priceArr.pop()
                    price = max(priceArr)
                else:
                    price = priceArr[0]
            
            naverItem = NaverAPI.findNaverAPI(title)
            
            if naverItem is None:
                image = 'http:' + item.select_one('.thumb_border').get('src')
                continue
            
            image = naverItem['image']
            
            if int(naverItem['lprice']) < price:
                # 배송비 추가 필요
                price = naverItem['lprice']
                link = naverItem['link']
            
            curs.execute(addSpecialPrice, (category, title, price, fee, image, link, last_crawling))
         
            last_crawling = False
         
    conn.commit()
   
    return {
        'statusCode': 200,
        'body': json.dumps('Community Crawling SUCCESS')
    }



class CrawlingUtil:
    def crawl(url):
        req = requests.get(url)
        html = req.text
        return BeautifulSoup(html, 'html.parser')
        

class NaverAPI:
    def findSearchNaver(item):
        url = 'https://search.shopping.naver.com/' + CrawlingUtil.crawl(item['link']).select_one('script').text.strip().split("'")[1]
        info = CrawlingUtil.crawl(url).select_one('._priceListMallLogo')
        return {'title':item['title'], 'lprice':item['lprice'], 'mallName':info.get('data-mall-name'), 'link':info.get('href'), 'image':item['image']}

    def findNaverAPI(itemName):
        url = "https://openapi.naver.com/v1/search/shop?query=" + urllib.parse.quote(itemName)  # json 결과
        request = urllib.request.Request(url)
        request.add_header("X-Naver-Client-Id", "NrksAQQEffia0Ek4iYdi")
        request.add_header("X-Naver-Client-Secret", "dopvU8BXFH")
        response = urllib.request.urlopen(request)
        rescode = response.getcode()
        if (rescode == 200):
            response_body = response.read()
        else:
            print("Error Code:" + rescode)

        items = json.loads(response_body)["items"]
        result = None

        if len(items) == 0:
            return result

        for item in items:
            if result is None:
                result = item
            result = item if int(item["lprice"]) < int(result["lprice"]) else result

        if result["productType"] == "1":
            result = NaverAPI.findSearchNaver(result)
        result["title"] = result["title"].replace("<b>", "").replace("</b>", "")

        return result