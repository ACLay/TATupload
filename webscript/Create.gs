function create(e){
  var sheetName = e.parameters.sheetName;
  try{
    var spread = buildSpread(sheetName);
    var oldSheet = spread.getSheets()[0];
    buildSheet(spread);
    spread.deleteSheet(oldSheet);
    return 'Sheet created successfully';
  }catch (err){
    return 'Unable to create + format sheet. Error description: ' + err.message;
  }
}

function buildSpread(sheetName){
  //create the spreadsheet
  var spreadsheet = SpreadsheetApp.create(sheetName);
  //save its ID
  saveSpreadsheetId(spreadsheet.getId());
  return spreadsheet;
}

function buildSheet(spreadsheet){
  //make a new sheet
  var sheet = spreadsheet.insertSheet();
  sheet.setName("Texts");
  //insert headers into the first row
  var range = sheet.getRange(1, 1, 1, 6);
  range.setValues([["Time received", "Their phone number", "Question", "Location", "Toastie flavours", "Original text"]]);
  /*for(var i = 1; i <= 6; i++){
    sheet.autoResizeColumn(i);//Not yet supported by new version of sheets
  }*/
  sheet.getRange(1,1,sheet.getMaxRows(),6).setWrap(true);
  //Save its ID
  saveSheetId(sheet.getSheetId());
}

function testBuild(){
  var spread = buildSpread("Test spreadsheet");
  var oldSheet = spread.getSheets()[0];
  buildSheet(spread);
  spread.deleteSheet(oldSheet);
  return 'Sheet created successfully';
}