package org.iata.ndc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Client
{
	public static void main( String args[] ) throws IOException
	{
		CloseableHttpClient httpClient = HttpClientBuilder.create().setUserAgent( "TestClient" ).build();

		try
		{
			// Make a request to the airline
			HttpPost request = new HttpPost( "API-ENDPOINT-URL-GOES-HERE" ); 
			
			InputStreamReader is = new InputStreamReader(Client.class.getResourceAsStream( "/resources/AirShoppingRQ.xml" ) );
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader( is );
			String read = br.readLine();

			while( read != null )
			{
				sb.append( read );
				read = br.readLine();
			}

			request.addHeader( "content-type", "application/xml;charset=utf-8" );
			request.addHeader( "Authorization-Key", "API-KEY-GOES-HERE" );

			StringEntity params = new StringEntity ( sb.toString() );
			request.setEntity( params );

			System.out.println( "Sending request body:" );
			System.out.println( sb.toString() );
			HttpResponse response = httpClient.execute( request );

			// Read the response
			is = new InputStreamReader( response.getEntity().getContent() );
			sb = new StringBuilder();
			br = new BufferedReader( is );
			read = br.readLine();

			while( read != null )
			{
				sb.append( read );
				read = br.readLine();
			}

			System.out.println( "------------------------------------------------------------" );
			System.out.println( "Received Response to request:" );
			System.out.println( sb.toString() );
			System.out.println( "------------------------------------------------------------" );

			// Use SAX to parse the document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			factory.setNamespaceAware( true );
			InputSource isource = new InputSource( new StringReader( sb.toString() ) );

			Document doc = builder.parse( isource );

			System.out.println( "Selecting 1st offer" );
			dumpChildren( doc.getElementsByTagName( "Offer" ).item( 0 ) );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
		finally
		{
			httpClient.close();
		}
	}
	
	private static void dumpChildren( Node node )
	{
		NodeList nl = node.getChildNodes();

		for ( int i = 0; i < nl.getLength(); i++ )
		{
			Node n = nl.item( i );
			
			if ( !"#text".equals( n.getNodeName() ) )
			{
				StringBuffer sb = new StringBuffer();
				sb.append( "Node " ).append( n.getNodeName() );

				NodeList nl2 = n.getChildNodes();
				int index = 0;
				while ( index < nl2.getLength() && !"#text".equals( nl2.item( index ).getNodeName() ) )
					index++;

				if ( index < nl2.getLength() )
				  sb.append( ", value = " ).append( nl2.item( index ).getNodeValue() );
				else
				  sb.append( ", this node has no value" );

				NamedNodeMap nnm = n.getAttributes();

				if ( nnm != null && nnm.getLength() > 0 )
				{
				  sb.append( "\nAttributes:\n" );

				  for ( int j = 0; j < nnm.getLength(); j++ )
					  sb.append( nnm.item( j ).toString() ).append( "\n" );
				}
				else
				  sb.append( "\nThis node has no attributes\n" );

				System.out.println( sb.toString() );

				if ( n.hasChildNodes() )
				  dumpChildren( n );
			}
		}
	}
}
