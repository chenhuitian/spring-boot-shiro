package bunkerchain.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class DateConverter {
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT);
    public static class Serialize extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator jgen, SerializerProvider provider) {
            try {
                if (value == null) {
                    jgen.writeNull();
                }
                else {
                     jgen.writeString(DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.systemDefault()).format(value) );
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static class Deserialize extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(com.fasterxml.jackson.core.JsonParser jp, DeserializationContext ctxt) throws IOException {
            try {
                String dateAsString = jp.getText();
                
                String dateRegex = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
               
                boolean isDate = dateAsString.matches(dateRegex);
                 
                if(isDate) {
                	dateAsString = dateAsString.concat(" 00:00:00");
                }
                
                if (dateAsString==null) {
                    return null;
                } else {
                    return Instant.ofEpochMilli(sdf1.parse(dateAsString).getTime());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}	