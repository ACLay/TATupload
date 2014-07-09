function submit(e){
  var sheetName = e.parameters.sheetName;
  var number = e.parameters.number;
  var question = e.parameters.question;
  var location = e.parameters.location;
  var toastie = e.parameters.toastie;
  var SMS = e.parameters.SMS;
  var time = e.parameters.time;
  
  try{
    submitSMS(sheetName, number, question, location, toastie, SMS, time);
    return 'Message submitted';
  }catch (err){
    return 'Unable to submit message. Error description: ' + err.message;
  }
}

function submitSMS(sheetName, number, question, location, toastie, SMS, time){
  var sheet = findSheet(sheetName);
  var row = [time[0], number[0], question[0], location[0], toastie[0], SMS[0]];//de-array needed with e.parameter here because...?
  sheet.appendRow(row);//atomic operation
}

function findSheet(sheetName){
  var folder = DocsList.getRootFolder();
  var files = folder.getFilesByType(DocsList.FileType.SPREADSHEET);
  var matches = 0;
  for(var i = 0; i < files.length; i++){
    if(files[i].getName() == sheetName){
      matches++;
      var ss = SpreadsheetApp.open(files[i]);
      var sheet = ss.getSheetByName(getSheetName());
      if(sheet != null){
        return sheet;
      }
    }
  }
  //Throw error if file or sheet cannot be found
  var error;
  if(matches == 0){
    throw {message:"Unable to find spreadsheet named '" + sheetName + "'"};
  } else {
    throw {message:"Unable to find a sheet named 'Texts' in '" + sheetName + "'"};
  }
}

function testSubmit(){
  try{
    submitSMS(["Jul 9, 2014 Text  toastie"], ["7654321"], ["which?"], ["somewhere"], ["cheese ham"], ["SMS thing"], ["tea time"]);
    return 'Message submitted';
  }catch (err){
    return 'Unable to submit message. Error description: ' + err.message;
  }
}