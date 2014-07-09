function create(e){
  var sheetName = e.parameters.sheetName;
  try{
    build(sheetName);
    return 'Sheet created successfully';
  }catch (err){
    return 'Unable to make and format sheet. Error description: ' + err.message;
  }
}

function build(sheetName){
  //create the spreadsheet
  var spreadsheet = SpreadsheetApp.create(sheetName);
  //rename the sheet
  var sheet = spreadsheet.getSheets()[0];
  sheet.setName(getSheetName());
  //insert headers into the first row
  var range = sheet.getRange(1, 1, 1, 6);
  range.setValues([["Time received", "Their phone number", "What do they want to know?", "Where are they?", "What toastie do they want?", "Original text"]]);
  /*for(var i = 1; i <= 6; i++){
    sheet.autoResizeColumn(i);//Not yet supported by new version of sheets
  }*/
  sheet.getRange(1,1,sheet.getMaxRows(),6).setWrap(true);
}

function testBuild(){
  build("Sheet");
}