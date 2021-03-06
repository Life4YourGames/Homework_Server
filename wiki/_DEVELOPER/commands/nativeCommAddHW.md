# Command: nativeCommAddHW
##### ID: de.mlessmann.commands.addhw

## Request
```  
{  
	"command": "addhw",  
	"homework": hwObj<JSONObject>  
}  
```  
  * ```hwObj``` - HomeWork object see doc for details  
  
  
## Response
If the homework was added successfully the response will be Status-201  
  
  
## Possible errors raised:  
* _InsufficientPermissionError_ - User is not allowed to add a homework  
* _AddHWError_ - HomeWork was invalid or an internal error occurred  
	- Status:500 Internal Error
	- Status:400 HomeWork invalid
* _ProtocolError_ - The request was invalid  
* _LoginRequiredError_ - User needs to login first  
  
  
## References:  
* Code implementation: [nativeCommAddHW.java](https://github.com/MarkL4YG/Homework_Server/blob/bleeding/src/main/java/de/mlessmann/network/commands/nativeCommAddHW.java)  
  
# 2nd USAGE!  
If the HomeWork objects contains an ID that already exists on the specified date, the server will attempt to edit the HomeWork.  
Meaning that the old HW will be overwritten with the new one.  
(This is actually how you're currently supposed to edit HomeWork entries)  
