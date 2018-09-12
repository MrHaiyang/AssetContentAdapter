package tv.jiaying.acadapter.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.jiaying.acadapter.entity.Item;
import tv.jiaying.acadapter.entity.ItemExtend;
import tv.jiaying.acadapter.entity.Provider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class XMLParseTool {

    private static Logger logger = LoggerFactory.getLogger(XMLParseTool.class);

    public Item parseXMLFileToItemClass(File xmlfile) throws DocumentException, ParseException {

        SAXReader reader = new SAXReader();
        //存储adi文件属性及属性值
        HashMap<String, String> adiMap = new HashMap<>();

        Document document = reader.read(xmlfile);
        Element adi = document.getRootElement();
        Iterator iterator = adi.elementIterator();

        while (iterator.hasNext()) {

            Element element = (Element) iterator.next();
            if ("Metadata".equals(element.getName())) {
                Element amsElement = element.element("AMS");
                List<Attribute> attributesOfams = amsElement.attributes();
                for (Attribute attribute : attributesOfams) {
                    adiMap.put(attribute.getName(), attribute.getValue());
                }
            } else if ("Asset".equals(element.getName())) {
                Iterator asserIt = element.elementIterator();
                while (asserIt.hasNext()) {

                    Element itemMetadata = (Element) asserIt.next();
                    if ("Metadata".equals(itemMetadata.getName())) {
                        List<Element> appDatas = itemMetadata.elements("App_Data");
                        for (Element appdata : appDatas) {
                            adiMap.put(appdata.attribute("Name").getValue(), appdata.attribute("Value").getValue());
                        }
                    } else if ("Asset".equals(itemMetadata.getName())) {
                        //跳过海报信息
                        for (int i = 0; i < 2; i++) {
                            if(asserIt.hasNext()){
                                asserIt.next();
                            }
                        }
                        itemMetadata = (Element) asserIt.next();
                        Element itemExtendMetadata = itemMetadata.element("Metadata");
                        List<Element> appDatas = itemExtendMetadata.elements("App_Data");
                        for (Element appdata : appDatas) {
                            adiMap.put(appdata.attribute("Name").getValue(), appdata.attribute("Value").getValue());
                        }

                    }
                }
            }

        }

        return parseAdiMapToItemClass(adiMap);
    }

    private Item parseAdiMapToItemClass(HashMap map) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Item item = new Item();
        Provider provider = new Provider();
        provider.setProviderId(map.get("Provider_ID").toString());
        provider.setName(map.get("Provider").toString());
        provider.setLogoTag(false);
        item.setProvider(provider);

        item.setAssetId(map.get("Asset_ID").toString());
        item.setProduct(map.get("Product").toString());
        item.setTitle(map.get("Title").toString());
        item.setRunTime(map.get("Run_Time").toString());
        switch (map.get("Show_Type").toString()) {
            case "电影":
                item.setShowType(Item.ShowType.movie);
                break;
            case "电视剧":
                item.setShowType(Item.ShowType.series);
                break;
            default:
                break;
        }
        item.setGenre(map.get("Genre").toString());

        Date licensingWindowStart = format.parse(map.get("Licensing_Window_Start").toString());
        Date licensingWindowEnd = format.parse(map.get("Licensing_Window_End").toString());
        item.setLicensingWindowStart(licensingWindowStart);
        item.setLicensingWindowEnd(licensingWindowEnd);

        item.setYear(map.get("Year").toString());
        item.setLanguage(map.get("Language").toString());
        item.setDirector(map.get("Director").toString());
        item.setActors(map.get("Actors").toString());
        item.setSummaryShort(map.get("Summary_Short").toString());
        Double suggestPrice=0.0;
        if(map.get("Suggested_Price")!=""){
            suggestPrice = Double.parseDouble(map.get("Suggested_Price").toString());
        }
        item.setSuggestedPrice(suggestPrice);

        item.setCountryOfOrigin(map.get("Country_of_Origin").toString());


        ItemExtend itemExtend = new ItemExtend();
        itemExtend.setFsContentFormat(map.get("FSContentFormat").toString());

        itemExtend.setContentFileSize(map.get("Content_FileSize").toString());

        itemExtend.setHDContent(map.get("HDContent").toString());
        itemExtend.setSubtitleLanguage(map.get("Subtitle_Languages").toString());
        int bitRate = Integer.parseInt(map.get("Bit_Rate").toString());
        itemExtend.setBitRate(bitRate);
        itemExtend.setAudioType(map.get("Audio_Type").toString());
        itemExtend.setCodec(map.get("Codec").toString());
        itemExtend.setFrameRate(map.get("Frame_Rate").toString());
        itemExtend.setResolution(map.get("Resolution").toString());
        itemExtend.setEncryption(map.get("Encryption").toString());

        item.setItemExtend(itemExtend);

        return item;
    }

    public String beanToXml(Object obj, Class<?> load) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);

        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();

    }

}
