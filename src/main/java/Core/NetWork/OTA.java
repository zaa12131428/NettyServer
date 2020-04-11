package Core.NetWork;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;

public class OTA {

    public static String httpClientUploadFile(String url, File file) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        String boundary ="--------------WebKitFormDkDcVNkfdvdvs";
        try {
            String fileName = file.getName();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setBoundary(boundary);
            builder.addPart("multipartFile",new FileBody(file));
            builder.addTextBody("filename", fileName,  ContentType.create("text/plain", Consts.UTF_8));
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
