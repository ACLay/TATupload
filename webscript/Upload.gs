function submit(e){
  var formName = e.parameters.formName;
  var number = e.parameters.number;
  var question = e.parameters.question;
  var location = e.parameters.location;
  var toastie = e.parameters.toastie;
  var SMS = e.parameters.SMS;
      
  var formID = findForm(formName);
  
  try{
    submitSMS(formID, number, question, location, toastie, SMS);
    return 'Message submitted';
  }catch (err){
    return 'Unable to submit message. Error description: ' + err.message;
  }
}

function submitSMS(formID, number, question, location, toastie, SMS){
  var form = FormApp.openById(formID);
  var items = form.getItems();
  var formResponse = form.createResponse();
  formResponse.withItemResponse(items[0].asTextItem().createResponse(number));
  formResponse.withItemResponse(items[1].asTextItem().createResponse(question));
  formResponse.withItemResponse(items[2].asTextItem().createResponse(location));
  formResponse.withItemResponse(items[3].asTextItem().createResponse(toastie));
  formResponse.withItemResponse(items[4].asTextItem().createResponse(SMS));
  
  formResponse.submit();                              
}
//This should throw an error if the file cannot be found
function findForm(formName){
  var folder = DocsList.getRootFolder();
  var files = folder.getFilesByType(DocsList.FileType.FORM);
 
  for(var i = 0; i < files.length; i++){  
    if(files[i].getName() == formName){
      return files[i].getId();
    }
  }
  
}