## Sample Java code for NDC Sandboxes connection

Sample Java code to demonstrate how to connect to the NDC Sandboxes and send / receive an NDC message which is loaded from the AirShoppingRQ.xml file.

A sample NDC request is supplied in the resources sub-directory and should be copied to the appropriate project path prior to executing the client.


Users should insert:

* The endpoint URL of the APIs in place of "API-ENDPOINT-URL-GOES-HERE", as defined on the NDC Developer Portal
* Their Mashery API key in the request header "Authorization-Key" in place of "API-KEY-GOES-HERE"

This code requires the following libraries which can be found at http://hc.apache.org/downloads.cgi under HttpClient:

* commons-logging
* httpclient
* httpcore

Users may modify the contents of the xml file to rapidly get started with their NDC interface testing.