# Distributed Crawler with Hadoop

## About
<img src = "/images/ui-homepage.png" width = "60%" align = "center">
Web crawler is one of the component of search engine that is used to download web pages on large quantities. 
This crawler application utilized the Hadoop ecosystem (HDFS and HBase) as the storage media for crawling results.

## Software used
This web crawler application can be used on both standalone and distributed environment. Below is the software that is used to develop this application.
1. The standalone version:
  * Java SE Development Kit (JDK) 1.8.0
  * HBase 1.2.6
  * Apache Tomcat 8.5.29
  * Cygwin (for computer that used non UNIX-based OS)
2. The distributed version:
  * Java SE Development Kit (JDK) 1.8
	* Hadoop 2.7.1
  * HBase 1.2.6
  * Apache Tomcat 7.0.76
  * Ubuntu 14.04 LTS 
  
## Features
1. Crawling and re-crawling based on the given URL seeds
2. URL seeds monitoring
![URL seeds monitoring page](/images/ui-front.png | width = "50%")
3. Crawling result monitoring
![Crawling results monitoring page](/images/ui-repo.png | width = "50%")
4. Searching 
![Searching results display](/images/ui-resusr.png | width = "50%")
