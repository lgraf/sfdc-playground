package metadata;

import com.sforce.soap.metadata.CustomFieldTranslation;
import com.sforce.soap.metadata.CustomObjectTranslation;
import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.PicklistValueTranslation;
import com.sforce.soap.metadata.ReadResult;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;


public class ReadPicklistValueTranslations
{
	private static final String API_VERSION = "35.0";

	private static final String USER = "";

	private static final String PASS = "";


	public static void main( String[] args ) throws ConnectionException
	{
		MetadataConnection connection = createMetadataConnection( USER, PASS );

		ReadResult result = connection.readMetadata( "CustomObjectTranslation", new String[] { "Lead-de" } );
		for( Metadata data : result.getRecords() )
		{
			CustomObjectTranslation objectTranslation = (CustomObjectTranslation)data;
			for( CustomFieldTranslation fieldTranslation : objectTranslation.getFields() )
			{
				if( fieldTranslation.getPicklistValues().length > 0 )
				{
					System.out.println( "Field: " + fieldTranslation.getName() );
					for( PicklistValueTranslation value : fieldTranslation.getPicklistValues() )
						System.out.println( "   " + value.getMasterLabel() + " -> " + value.getTranslation() );
				}
			}
		}
	}


	private static MetadataConnection createMetadataConnection( String user, String password )
			throws ConnectionException
	{
		LoginResult result = login( user, password );

		ConnectorConfig config = new ConnectorConfig();
		config.setServiceEndpoint( result.getMetadataServerUrl() );
		config.setSessionId( result.getSessionId() );

		return com.sforce.soap.metadata.Connector.newConnection( config );
	}


	private static LoginResult login( String username, String password ) throws ConnectionException
	{
		final String URL = "https://login.salesforce.com/services/Soap/u/" + API_VERSION;

		ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint( URL );
		config.setServiceEndpoint( URL );
		config.setManualLogin( true );

		PartnerConnection connection = com.sforce.soap.partner.Connector.newConnection( config );
		return connection.login( username, password );
	}
}