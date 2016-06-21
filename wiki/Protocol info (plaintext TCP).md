# Syntax-Help for Syntax version 1.1.0.X
##### Last updated: 2016-06-23T17:34:00+0200

This is the Syntax and command information for unencrypted TCP connections to the server  
- Default tcp-plaintext-port is 11900  
- Messages end with ```CRLF``` or ```LF```
- Communication is in valid JSON [see JSON.org](http://json.org)  
- Command is a sample of what the client should send  
- Response is the response of the server if successful  
  
  
## Status:
The server provides status information in every response  
As expected it will be embedded into JSON with two fields:  
- "status"\<int\> from [Status.java](https://github.com/MarkL4YG/Homework_Server/blob/Latest/src/main/java/de/mlessmann/network/Status.java)  

## Responses:  
Any response of the server will follow this pattern:  
```
{  
	"status"<int>: <status>,  
	"payload_type"<String>: <Type of payload>,  
	"payload"<see above>: <payload>	 
	(, "array_type"<String>: <type of array content>)
}  
```
  
Valid Payload (and Array) types are:  
* JSONObject  
* JSONArray (will include "array_type" field)
* HWObject  
* Int (JSON - integer)  
* Str (JSON - String)  
* null (no payload)  
* float (JSON - float)  





## Set group
(IDs for testinstance: "test_1", "test_2")
### Command:
```
{
	"command": "setgroup",
	"parameters": ["<groupident>", "<unused>", "<unused>"]
}
```
### Response:
```
{
	<STATUS_OK>
}
```

## Add a HomeWork
### Command:
```
{
	"command": "addhw",
	"homework": {<HomeWork-Object>}
}
```
### Response:
```
{
	<STATUS_CREATED>
}
```

## Get a HomeWork by date
### On X
#### Command
{
	"command": "gethw",
	"date": [(int)yyyy, (int)MM, (int)dd]
}

### Between X and Y
#### Command:
{
	"command": "gethw",
	"fromdate": [(int)yyyy, (int)MM, (int)dd],
	"todate": [(int)yyyy, (int)MM, (int)dd]
}

### Response
```
{
	<STATUS_OK>,
	"type": "hw_array",
	"payload": [<ARRAY_OF_HOMEWORK>]
}
```
## Delete a HomeWork
```
{
	"command": "delhw",
	"date": [<see above>],
	"id": "<id from HomeWork-Object>"
}
````

###### .  

## ::IMPORTANT: ALWAYS CHECK THE SERVERs SYNTAX VERSION!::  

###### . 

## GetInfo
### Protocoll information
#### Command
(this command is guarranteed to be accepted ALWAYS by ANY Server version)
```
{  
	"command": "getInfo",  
	"cap": "proto"  
}  
```
 OR
```
 "protoInfo"
```
(THIS is the ONLY case in which non-json is accepted!)
#### Response:
Response will ALWAYS contain AT LEAST this:
```
{
	"protoVersion": "MAJOR.MINOR.RELEASE.BUILD"
}
```
##### Details on if something does not match:  
- MAJOR: NONE or at least one critical of the commands is compatible
- MINOR: Some or major part of commands are INCOMPATIBLE  
- RELEASE: Not all commands () are supported, but those who are should work (DOES NOT MEAN IT'S COMPATIBLE!)  
- BUILD: Some aditional commands or parameters may be available  


# TODO: Add more information and commands  