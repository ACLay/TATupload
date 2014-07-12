function saveSpreadsheetId(id){
  var properties = PropertiesService.getUserProperties();
  properties.setProperty(ssKey(), id);
}

function saveSheetId(id){
  var properties = PropertiesService.getUserProperties();
  properties.setProperty(sheetKey(), id);
}

function getSpreadsheetId(){
  var properties = PropertiesService.getUserProperties();
  var id = properties.getProperty(ssKey());
  return id;
}

function getSheetId(){
  var properties = PropertiesService.getUserProperties();
  var id = properties.getProperty(sheetKey());
  return id;
}

function ssKey(){
  return "spreadsheet_ID";
}

function sheetKey(){
  return "sheet_ID";
}