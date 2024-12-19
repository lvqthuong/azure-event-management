# Event Management System API

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Azure](https://img.shields.io/badge/Azure-Enabled-blue)
![ASP.NET Core](https://img.shields.io/badge/ASP.NET%20Core-8.0-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Enabled-blue)

## Project Description

The Event Management System is a web application built with Java Spring Boot, Hibernate, and PostgreSQL. It integrates with Azure services for authentication, authorization, storage and monitoring.

## Features

1. **User registration and authentication** with Azure AD B2C
2. **Role-based authorization** (Admin, EventProvider, User).
3. **CRUD operations** for events.
4. **Event registration with FIFO processing** using Azure Service Bus and Azure Functions.
5. **Storage of application data** in Cosmos DB for PostgreSQL.
6. **Storage of images and documents** in Azure Blob Storage.
7. **Caching of events & attachments metadata** with Azure Cache for Redis.
8. **Monitoring and diagnostics** with Azure Application Insights.

## Database Structure

- **Cosmos DB for PostgreSQL**
  - **Users:** Stores user information including authentication details.
  - **Events:** Stores details of each event and metadata.
  - **EventsRegistrations:** Tracks which users have registered for which events and the status of the registration.
  - **EventsAttachments:** Store the metadata of event attachments (images, videos, documents).

- **Azure Blob Storage**
  - Single storage container for all attachments.

- **Azure Cache for Redis**
  - 

## Workflow

1. **User accesses the Event Management System web app and signs in.**
2. **Browser pulls static resources from Azure CDN.**
3. **User searches for events by metadata.** The web app checks Redis for cached search results.
4. **If cache miss,** the web app queries Cosmos DB for event data and stores the results in Redis.
5. **Web app retrieves event details from Redis** if available, otherwise from Cosmos DB, and updates the cache.
6. **Pulls event-related images and documents from Azure Blob Storage.**
7. **User registers/unregisters for an event.** Registration information is placed in an Azure Service Bus queue with sessions enabled.
8. **Azure Functions processes the registration/unregistration** from the Service Bus queue, ensuring FIFO order, use transaction to modify event's properties.
9. **Azure Functions updates the registration status in Cosmos DB** and may trigger other necessary actions such as sending confirmation emails.
10. **Application Insights monitors and diagnoses issues** in the application.

## Alternative account management strategy
- Instead of managing ApplicationUser in the code base, you can consider using EntraID & Microsoft Graph to manage all registration. login, authentication (In this case, there will be no ApplicationUser in your code base, but only use UserId in Event registration. UserId will be taken from EntraId)
