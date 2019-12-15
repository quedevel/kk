
import sys
import logging
import pymysql
import urllib.request
import requests
import re
from bs4 import BeautifulSoup

def lambda_handler(event, context):
    conn = pymysql.connect(host='jarvis1.clzkjwwf2qt9.ap-northeast-2.rds.amazonaws.com', user='admin', passwd='rlxhdqks001!', db='jarvis')
    curs = conn.cursor()
    
    selectLastData = 'select title from tbl_community_crawling where last_crawling = 1 order by no desc limit 1'
    
    addSpecialPrice = 'insert into tbl_community_crawling (category, title, price, image, link, last_crawling) values(%s, %s, %s, %s, %s, %s)'
    
    curs.execute(selectLastData)
    
    lastData = curs.fetchone()
    
    if lastData != None:
        lastData = lastData[0]
    
    url = "http://www.ppomppu.co.kr/zboard/zboard.php?id=ppomppu&page=1&divpage=58"
    soup = CrawlingUtil.crawl(url)

    list = soup.select('.list0, .list1')

    last_crawling = True

    for item in list:
        titleTag = item.select_one('.list_title')
        if titleTag:
            title = re.sub('\(.+?\)', '', re.sub('\[.+?\]', '', titleTag.text)).strip()
           
            if title == lastData:
                break
 
            priceTag = re.search('\(.+?\)', titleTag.text)
            boardLink = 'http://www.ppomppu.co.kr/zboard/' + item.select('a')[1].get('href')

            link = CrawlingUtil.crawl(boardLink).select_one('.wordfix a').get('href')
            category = item.select_one('nobr.list_vspace').text
            image = 'http:' + item.select_one('.thumb_border').get('src')
            
            if priceTag:
                price = re.sub('[(),\u3131-\u3163\uac00-\ud7a3]+', '', priceTag.group()).strip().split('/')
    
            curs.execute(addSpecialPrice, (category, title, 10000, image, link, last_crawling))
         
            last_crawling = False
         
    conn.commit()
   
#   return {
#         'statusCode': 200,
#         'body': json.dumps('Hello from Lambda!')
#     }



class CrawlingUtil:
    def crawl(url):
        req = requests.get(url)
        html = req.text
        return BeautifulSoup(html, 'html.parser')