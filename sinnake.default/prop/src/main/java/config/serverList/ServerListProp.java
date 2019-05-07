package config.serverList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.gson.Gson;

@Configuration
public class ServerListProp {
	private String propFile;
	private String serverType;
	private Map<String, ServerList> serverlist = new HashMap<>();
	
	@Autowired
	public ServerListProp(@Value("#{globalProp['serverListPropFile.propertie']}") String propFile
		, @Value("#{globalProp['type.propertie']}") String serverType) throws Exception {

		this.propFile = propFile;
		this.serverType = serverType;
		
		this.setServerList();
	}
	
	public Map<String, ServerList> getServerList() {
		return this.serverlist;
	}
	
	public ServerList getServerList(String serverName) {
		return this.serverlist.get(serverName);
	}

	@SuppressWarnings("unchecked")
	protected void setServerList()  throws Exception {				
		String jsonString = this.getJsonString();

		HashMap<String, Object> prop = new Gson().fromJson(jsonString, HashMap.class);		
		
		Iterator<String> it = prop.keySet().iterator();		
		while(it.hasNext()) {
			String key = it.next();
			
			Map<String, Object> val = (Map<String, Object>)prop.get(key);
			
			this.serverlist.put(key, new ServerList(val.get("eurekaServiceId").toString()
				, (Map<String, String>)val.get("list")));
		}
	}
	
	protected String getJsonString()  throws Exception {
		String result = "";
		String line = "";
		
		ClassPathResource classPathResource = new ClassPathResource(this.propFile.replace("${serverType}", this.serverType));
		BufferedReader br = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(), "UTF-8"));
		
		while((line = br.readLine()) != null) { result += line.trim(); }
		
		return result;
	}
	
	public class ServerList {
		private String eurekaServiceId;
		private Map<String, String> list;
		
		public ServerList(String eurekaServiceId, Map<String, String> list) {
			this.eurekaServiceId = eurekaServiceId;
			this.list = list;
		}

		public String getEurekaServiceId() { return eurekaServiceId; }
		public Map<String, String> getList() { return list; }		
		public String getHttpAddress() { return "http://" + this.eurekaServiceId; }
		public String getHttpsAddress() { return "https://" + this.eurekaServiceId; }
		public String getHttpFullAddress(String listKey) { return this.getHttpAddress() + Optional.of(this.list.get(listKey)).get(); }		
		public String getHttpsFullAddress(String listKey) { return this.getHttpsAddress() + Optional.of(this.list.get(listKey)).get(); }
	}
}
