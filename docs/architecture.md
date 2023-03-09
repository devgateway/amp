---
layout: page
title: Architecture
permalink: /architecture/
---

## Technologies
AMP 3.5 is deployed using [Apache Tomcat 8.5](https://tomcat.apache.org/download-80.cgi#8.5.85), an open-source Web Server that implements several Java EE specifications. AMP is based on [Java 8](https://java.com/en/download/faq/java8.xml). The open-source [Nginx HTTP Server](https://www.nginx.com/) is used to deliver the application content to the country-specific domain, with the right pointers to the Public Portal and AMP itself. AMP benefits from its security, traffic control efficiency and its compliance with current HTTP standards. AMP developer setup additionally uses some tools like [Maven 3.8.1](https://maven.apache.org) and [GitHub](https://github.com/devgateway/amp). The AMP Public Portal is deployed as a separate application and can even run on a different server if necessary. The Public Portal uses WordPress.

DG leverages Open Source technologies both as part of the end-product applications as well as its supporting tools in the Software Development process. Our Aid Management Platform relies upon Jenkins for continuous integration. DG uses GitHub extensively in its development process, and our developers are active participants in the Open Source community, with experience in all common Open Source practices.

### PostgreSQL
AMP system relies on an open-source [PostgreSQL](https://www.postgresql.org) Database. It is well known for its high performance, and reliability and, unlike some other open-source RDBMS, it provides a [PostGIS extension](http://postgis.net) that we use for the GIS module. PostgreSQL fulfilled AMP system needs and proved stable over an extensive period of usage in different environments with different resources. We have defined a set of configurations to tune PostgreSQL for AMP needs to gain the best performance for each country setup.
Document Management is handled by the open-source content repository for the Java platform, [Apache Jackrabbit](http://jackrabbit.apache.org/jcr/index.html). 

### Backend
The Aid Management Platform 3.0 backend runs on the latest Java 8 release. Java 8 includes features like Lambda Expressions, new Date/Time API. The Java-based platform brings us a multitude of Java frameworks, cross-platform capability, a large pool of technical knowledge shared on the Internet and hard copy books, as well the possibility to hand over to any other third-party Java experienced developers.

AMP uses Hibernate 4.x core and Hibernate 2.x for Entities mappings to RDBS DB. Using Java Persistence API allows us to switch to a different RDBS, though PostgreSQL proved to be the right long-term choice.

The open-source [Spring](https://spring.io) 3.x Security framework is used to provide AMP authentication and authorization with its protection against attacks like session fixation, clickjacking, and cross-site request forgery, among others.

[Apache Lucene](https://lucene.apache.org/core/) open-source search software enables Java-based indexing and advanced search capabilities among AMP data like activities, reports, documents, etc.

[Struts](https://struts.apache.org/) 1.x Forms and Actions is used in part of the admin sections of the application.

For the complex features of the Project Edition Form, we use [Apache Wicket](http://wicket.apache.org/) open source, component-oriented, serverside, Java web application framework. First released in 2004, it is one of the few serverside web frameworks that continue to be in use today and still proves to be one of the best choices for special needs.

AMP API was developed on [Jersey](https://jersey.java.net/) 1.x RESTful Web Service open source framework for Java that provides support for JAX-RS APIs and servers as JAX-RS ([JSR-311](https://jcp.org/en/jsr/detail?id=311) & [JSR 339](https://jcp.org/en/jsr/detail?id=311)) Reference Implementation.

For various automated Jobs like Auto closing activities, and alert notifications, AMP relies upon [Quartz](http://www.quartz-scheduler.org/) Job scheduling open source library. It allows us to have a very-grained configuration of the schedules and run multiple jobs concurrently.

### Frontend

AMP evolved in the context of mid-2000’ technologies, revolving periodically to more modern ones. The system is quite big, and amid AMP growth and periodic reengineering, we have some legacy areas. Legacy's front-end is based on [JSP](https://jsp.java.net/) with [yUI](http://yuilibrary.com/), [jQuery](https://jquery.com/), and JavaScript.

Along with different reengineering efforts, revamped most relevant modules UI like GIS, Dashboards, Tabs, Filters, and some Admin parts,  that now embrace a new look and feel. Modern JS libraries and tools are used to ease development and maintenance. The decoupling from the AMP system via its API allowed us to address each AMP module's particular needs.

Charts are built with the help of [D3.js](https://d3js.org/), a JavaScript library for data-driven documents. D3.js combines powerful visualization components with a data-driven approach to DOM manipulation, emphasizing web standards. We also reuse the pre-build D3 components provided by [NVD3](http://nvd3.org/).  We built new charts and new areas of the admin side using [ReactJS](https://reactjs.org) and [ReduxJS](https://redux.js.org/) together with [Nivo](https://nivo.rocks/) to give the user unique UI/UX experiences.

We reuse a free and open-source collection of tools provided by [Bootstrap](http://getbootstrap.com/) that includes HTML and CSS-based templates for forms, buttons, navigation, and other interface components, as well as optional JavaScript extensions. 
[Backbone](http://backbonejs.org/) allowed us a quicker development of models with its support for key-value binding, custom events, collections with a rich API of enumerable functions, and views with declarative event handling that could be easily connected to AMP API. These contributed to the easy development of different elements for Reports UI.

Open source [jqGrid](http://jqgrid.com/) plugin on top of jQuery came in to support the Tabs results display in a simple grid view, with its support for pagination, AJAX.

To provide an interactive GIS experience, AMP relied upon [Leaflet](https://leafletjs.com/), [Esri-Leaflet](https://esri.github.io/esri-leaflet/) and [React-LeaftLet](https://react-leaflet.js.org/) plugins. Built with simplicity, performance, and usability in mind, it enabled us with a full set of mapping features, as well as mobile-friendly support. Esri-Leaflet provided us with tools for consuming ArcGIS services with Leaflet.