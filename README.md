# Distributed Crawler with Hadoop
<center><img src = "/images/ui-homepage.png" width = "80%"></center>

## About
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
<center><img src = "/images/ui-front.png" width = "80%"></center>
3. Crawling result monitoring
<center><img src = "/images/ui-repo.png" width = "80%"></center>
4. Searching 
<center><img src = "/images/ui-resusr.png" width = "80%"></center>
