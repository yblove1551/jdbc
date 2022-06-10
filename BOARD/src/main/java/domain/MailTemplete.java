package domain;

import org.json.simple.JSONObject;

public class MailTemplete {
	private int kind;
	private String title;
	private String content;
	private String description;
	private String use_yn;
	
	@Override
	public String toString() {
		return "MailTemplete [kind=" + kind + ", title=" + title + ", content=" + content + ", description=" + description
				+ ", use_yn=" + use_yn + "]";
	}
	
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	
	public JSONObject toJSONObj() {
		JSONObject obj = new JSONObject();
		obj.put("kind", kind);
		obj.put("title", title);
		obj.put("content", content);
		obj.put("description", description);
		obj.put("use_yn", use_yn);
		return obj;
	}
	
	
	
	
}
