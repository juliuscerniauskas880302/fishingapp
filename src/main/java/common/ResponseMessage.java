package common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
public class ResponseMessage {
    private String content;

    public ResponseMessage() {
        super();
    }

    public ResponseMessage(String content) {
        super();
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
