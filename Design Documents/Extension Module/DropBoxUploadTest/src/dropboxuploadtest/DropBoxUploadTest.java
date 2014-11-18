/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropboxuploadtest;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author ybar
 */
public class DropBoxUploadTest {

   private static final String APP_KEY = "damxt9eg60firlf";
   private static final String APP_SECRET = "evyahqu99d05stw";
   final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
   private static DropboxAPI<WebAuthSession> mDBApi;
   
    public static void main(String[] args)
    {
          AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
          WebAuthSession session = new WebAuthSession(appKeys, ACCESS_TYPE);
          mDBApi = new DropboxAPI<WebAuthSession>(session);

          try
          {
              File tokensFile = new File("E:/in.txt");
              Scanner tokenScanner = new Scanner(tokensFile);
              
              String ACCESS_TOKEN_KEY = tokenScanner.nextLine();    // Read key
              String ACCESS_TOKEN_SECRET = tokenScanner.nextLine(); // Read secret
              tokenScanner.close(); //Close Scanner
              //Re-auth
              AccessTokenPair reAuthTokens = new AccessTokenPair(ACCESS_TOKEN_KEY, ACCESS_TOKEN_SECRET);
              mDBApi.getSession().setAccessTokenPair(reAuthTokens);
              //  put Pic1.jpg file in the current directory.
              File f = new File("E:/Pic2.jpg");
              InputStream inputStream = new FileInputStream(f);
              mDBApi.putFile("/Photos/", inputStream, f.length(), null, null);
              inputStream.close();
              
          }
          catch(Exception e)
          {
              e.printStackTrace();
          }    
         
    }
}
