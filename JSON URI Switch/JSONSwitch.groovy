//////////////////////////////////////////////////////
//													//
//	   		Copywright 2020 Cris Beagle				//
//													//
//			This will create a switch 				//
//			that will post JSON to a URL			//
//			with a ON parameter, and an				//
//			OFF parameter. Tested with				//
//			Adafuit.io. Really anthing 				//
//			can be placed in the body				//. 
//													//
//			Inspired by:							//
//				https://github.com/tguerena			//
//													//
//													//
//////////////////////////////////////////////////////


metadata {
    definition (name: "JSON Switch", namespace: "linuxx", author: "Cris Beagle") {
    capability "Switch"
    
    preferences {

        //the URL
        input name: "url", type: "text", title: "URL", description: "The URL to POST to", required: true

        //input for the on
        input name: "on", type: "text", title: "JSON On Body", description: "The request body for on", required: true

        //input for the off
        input name: "off", type: "text", title: "JSON Off Body", description: "The request body for off", required: true

	}
}

// simulator metadata
simulator {
}

// UI tile definitions
tiles {
	standardTile("Switch", "device.switch", width: 2, height: 2, canChangeIcon: false) {
		state "on", label: 'On', action: "switch.off", icon: "st.Lighting.light13", backgroundColor: "#00AA00"
		state "off", label: 'Off', action: "switch.on", icon: "st.Lighting.light13", backgroundColor: "#AAAAAA"
	}
	standardTile("OnButton", "device.button", width: 1, height: 1, canChangeIcon: false) {
		state "pushed", label: 'On', action: "switch.on", icon: "st.Kids.kid10"
	}
	standardTile("OffButton", "device.button", width: 1, height: 1, canChangeIcon: false) {
		state "pushed", label: 'Off', action: "switch.off", icon: "st.Kids.kid10"
	}
	main "Switch"
	details(["Switch","OnButton","OffButton"])
    
}
}

def parse(String description) {
}

def on() {
	log.debug "Firing the on command"
    
    def params = [
		uri: "$settings.url",
		body: "$settings.on"
	]

	//log just in case
	log.debug params

	try {
		httpPostJson(params) { resp ->
			resp.headers.each {
				log.debug "${it.name} : ${it.value}"
			}
			log.debug "response contentType: ${resp.contentType}"
		}
        sendEvent(name: "switch", value: "on", isStateChange: "true")
	} catch (e) {
		log.debug "something went wrong: $e"
	}
}

def off() {
	log.debug "Firing the off command"

	def params = [
	uri: "$settings.url",
	body: "$settings.off"
	]
	
    //log just in case
    log.debug params
    
	try {
		httpPostJson(params) { resp ->
			resp.headers.each {
				log.debug "${it.name} : ${it.value}"
			}
			log.debug "response contentType: ${resp.contentType}"
		}
        sendEvent(name: "switch", value: "off", isStateChange: "true")
	} catch (e) {
		log.debug "something went wrong: $e"
	}
}