package tv.jiaying.acadapter.processor.cdn;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tv.jiaying.acadapter.util.XMLParseTool;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Date;

/**
 * cdn接口服务
 */
@Component
public class CDNTransfer implements Transfer {

    private static Logger logger = LoggerFactory.getLogger(CDNTransfer.class);

    @Autowired
    TransferConfig transferConfig;

    /**
     * 注入内容接口
     *
     * @param param
     * @return
     */
    @Override
    public int transferContent(TransferContentParam param) {

        String xmlParam;

        int statusCode = 0;

        try {
            //把参数转换成接口需要的xmlString
            xmlParam = new XMLParseTool().beanToXml(param, TransferContentParam.class);
            //logger.info("{}", xmlParam);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            logger.info("url:{}", transferConfig.getContentUrl());
            HttpPost httpPost = new HttpPost(transferConfig.getContentUrl());
            StringEntity xmlEntity = new StringEntity(xmlParam, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml; charset=UTF-8");
            httpPost.addHeader("Date", new Date().toString());
            //httpPost.addHeader("Content-Length", "1039");
            httpPost.setEntity(xmlEntity);

            //String retSrc;
            //int statusCode = 0;
            try {
                response = httpclient.execute(httpPost);
                statusCode = response.getStatusLine().getStatusCode();
                logger.info("code:{},reason:{}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                //org.apache.http.HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    retSrc = EntityUtils.toString(entity);
//                    //logger.info("retSrc:{}", retSrc);
//                }
            } catch (IOException e) {
                logger.info(e.getMessage(), e);
            } finally {
                response.close();
            }

//            if (statusCode == 200) {
//                return true;
//            }


        } catch (JAXBException e) {
            logger.info(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
        return statusCode;
    }

    /**
     * 查询注入状态接口
     *
     * @param param
     * @return
     */
    @Override
    public Object getTransferStatus(GetTransferStatusParam param) {
        String xmlParam;
        try {
            //把参数转换成接口需要的xmlString
            xmlParam = new XMLParseTool().beanToXml(param, GetTransferStatusParam.class);
            //logger.info("{}", xmlParam);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            logger.info("url:{}", transferConfig.getTransferStatusUrl());
            HttpPost httpPost = new HttpPost(transferConfig.getTransferStatusUrl());
            StringEntity xmlEntity = new StringEntity(xmlParam, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml; charset=UTF-8");
            httpPost.addHeader("Date", new Date().toString());
            //httpPost.addHeader("Content-Length", "1039");
            httpPost.setEntity(xmlEntity);

            String retSrc = null;
            int statusCode = 0;
            try {
                response = httpclient.execute(httpPost);
                statusCode = response.getStatusLine().getStatusCode();
                logger.info("code:{},reason:{}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());

                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    retSrc = EntityUtils.toString(entity);
                    //logger.info("retSrc:{}", retSrc);
                }
            } catch (IOException e) {
                logger.info(e.getMessage(), e);
            } finally {
                response.close();
            }

            if (statusCode == 200) {
                return retSrc;
            }

        } catch (JAXBException e) {
            logger.info(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询媒资文件在cdn是否存在
     *
     * @param param
     * @return
     */
    @Override
    public Object getContentInfo(GetContentInfoParam param) {

        String xmlParam;
        try {
            //把参数转换成接口需要的xmlString
            xmlParam = new XMLParseTool().beanToXml(param, GetContentInfoParam.class);
            //logger.info("{}", xmlParam);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            logger.info("url:{}", transferConfig.getContentInfoUrl());
            HttpPost httpPost = new HttpPost(transferConfig.getContentInfoUrl());
            StringEntity xmlEntity = new StringEntity(xmlParam, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml; charset=UTF-8");
            httpPost.addHeader("Date", new Date().toString());
            //httpPost.addHeader("Content-Length", "1039");
            httpPost.setEntity(xmlEntity);

            String retSrc;
            int statusCode = 0;
            try {
                response = httpclient.execute(httpPost);
                statusCode = response.getStatusLine().getStatusCode();
                logger.info("code:{},reason:{}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            } catch (IOException e) {
                logger.info(e.getMessage(), e);
            } finally {
                response.close();
            }

            if (statusCode == 200) {
                return true;
            }

        } catch (JAXBException e) {
            logger.info(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Boolean deleteContent(DeleteContentParam param) {

        String xmlParam;
        try {
            //把参数转换成接口需要的xmlString
            xmlParam = new XMLParseTool().beanToXml(param, DeleteContentParam.class);
            //logger.info("{}", xmlParam);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            logger.info("url:{}", transferConfig.getDeleteContentUrl());
            HttpPost httpPost = new HttpPost(transferConfig.getDeleteContentUrl());
            StringEntity xmlEntity = new StringEntity(xmlParam, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml; charset=UTF-8");
            httpPost.addHeader("Date", new Date().toString());
            //httpPost.addHeader("Content-Length", "1039");
            httpPost.setEntity(xmlEntity);

            String retSrc;
            int statusCode = 0;
            try {
                response = httpclient.execute(httpPost);
                statusCode = response.getStatusLine().getStatusCode();
                logger.info("code:{},reason:{}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            } catch (IOException e) {
                logger.info(e.getMessage(), e);
            } finally {
                response.close();
            }

            if (statusCode == 200) {
                return true;
            }

        } catch (JAXBException e) {
            logger.info(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Boolean cancelTransfer() {
        return null;
    }

}
