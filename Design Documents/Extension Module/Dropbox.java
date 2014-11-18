import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.Session.AccessType;

public class DropBoxAuthTest 
{

  // App key & secret that Dropbox's developer website gives your app
 // Key Received @ step 1.5
 private static final String APP_KEY = "your_key";
 // Secret Received @ step 1.5
 private static final String APP_SECRET = "your_secret";
 // Permission type created @ step 1.4
 final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;
 private static DropboxAPI<WebAuthSession> mDBApi;

  public static void main(String[] args) throws Exception {
  
  // Initialize the goods/session
  AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
  WebAuthSession session = new WebAuthSession(appKeys, ACCESS_TYPE);
  // Initialize DropboxAPI object
  mDBApi = new DropboxAPI<WebAuthSession>(session);
  // Get ready for user input
  Scanner input = new Scanner(System.in);
  // Open file that stores tokens, MUST exist as a blank file
  File tokensFile = new File("TOKENS");
  System.out.println("Enter 'a' to authenticate, or 'r' to reauthentication, 'd' to download, 'u' to  upload ");
  String command = input.next();
  if(command.equals("a")){
   try {
    // Present user with URL to allow app access to Dropbox account on
    System.out.println("Please go to this URL and hit \"Allow\": " + DBApi.getSession().getAuthInfo().url);
    AccessTokenPair tokenPair = mDBApi.getSession().getAccessTokenPair();
    // Wait for user to Allow app in browser
    System.out.println("Finished allowing?  Enter 'next' if so: ");
    if(input.next().equals("next")){
     RequestTokenPair tokens = new RequestTokenPair(tokenPair.key,tokenPair.secret);
     mDBApi.getSession().retrieveWebAccessToken(tokens);
     PrintWriter tokenWriter = new PrintWriter(tokensFile);
     tokenWriter.println(session.getAccessTokenPair().key);
     tokenWriter.println(session.getAccessTokenPair().secret);
     tokenWriter.close();
     System.out.println("Authentication Successful!");
    }
   } catch (DropboxException e) {
    e.printStackTrace();
   }
  } else if(command.equals("r")){
   // Initiate Scanner to read tokens from TOKEN file
   Scanner tokenScanner = new Scanner(tokensFile);    
   String ACCESS_TOKEN_KEY = tokenScanner.nextLine();    // Read key
   String ACCESS_TOKEN_SECRET = tokenScanner.nextLine(); // Read secret
   tokenScanner.close(); //Close Scanner
   //Re-auth
   AccessTokenPair reAuthTokens = new AccessTokenPair(ACCESS_TOKEN_KEY,ACCESS_TOKEN_SECRET);
   mDBApi.getSession().setAccessTokenPair(reAuthTokens);
   System.out.println("Re-authentication Sucessful!");
   //Run test command
   System.out.println("Hello there, " + mDBApi.accountInfo().displayName);
  } else if(command.equals("d")){
   // Initiate Scanner to read tokens from TOKEN file
   Scanner tokenScanner = new Scanner(tokensFile);       
   String ACCESS_TOKEN_KEY = tokenScanner.nextLine();    // Read key
   String ACCESS_TOKEN_SECRET = tokenScanner.nextLine(); // Read secret
   tokenScanner.close(); //Close Scanner
   //Re-auth
   AccessTokenPair reAuthTokens = new AccessTokenPair(ACCESS_TOKEN_KEY,ACCESS_TOKEN_SECRET);
   mDBApi.getSession().setAccessTokenPair(reAuthTokens);
   Entry entries = mDBApi.metadata("/", 20, null, true, null);
   for (Entry e: entries.contents) {
    if(!e.isDeleted){
     if(e.isDir){
      System.out.println("Folder ---> " + e.fileName() );
     } else {
      //  this will download the root level files.
      System.out.println("File ---->" + e.fileName());
      DropboxInputStream inputStream = mDBApi.getFileStream(e.path,null);
      OutputStream out=new FileOutputStream(e.fileName());
      byte buf[]=new byte[1024];
      int len;
      while((len=inputStream.read(buf))>0)
       out.write(buf,0,len);
           out.close();
           inputStream.close();
           System.out.println("File is created....");
     }
    }
   }
  } else if(command.equals("u")){
   // Initiate Scanner to read tokens from TOKEN file
   Scanner tokenScanner = new Scanner(tokensFile);    
   String ACCESS_TOKEN_KEY = tokenScanner.nextLine();    // Read key
   String ACCESS_TOKEN_SECRET = tokenScanner.nextLine(); // Read secret
   tokenScanner.close(); //Close Scanner
   //Re-auth
   AccessTokenPair reAuthTokens = new AccessTokenPair(ACCESS_TOKEN_KEY, ACCESS_TOKEN_SECRET);
   mDBApi.getSession().setAccessTokenPair(reAuthTokens);
   //  put Pic1.jpg file in the current directory.
   File f = new File("Pic2.jpg");
   InputStream inputStream = new FileInputStream(f);
   mDBApi.putFile("/Photos/", inputStream, f.length(), null, null);
   inputStream.close();
  }
 }
}