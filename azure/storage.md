## Storage blob

az storage blob directory list --sas-token "sp=racwdli&st=2022-01-21T07:13:13Z&se=2032-01-20T15:13:13Z&spr=https&sv=2020-08-04&sr=c&sig=CRDCnPpsMyL9V0NjSbTVRtrRv8iNoBhG6atpJxL0A2E%3D"


az storage container list --account-name devretailappblob --sas-token "?sv=2020-08-04&ss=bf&srt=sco&sp=rwdlacitfx&se=2032-01-21T18:46:13Z&st=2022-01-21T10:46:13Z&spr=https&sig=WoerwGe%2BAc%2BkOU5hTzAMY2qZa8YE8Y3ayh8cFCfgtIE%3D"

az storage file list --account-name devretailappblob --sas-token "?sv=2020-08-04&ss=bf&srt=sco&sp=rwdlacitfx&se=2032-01-21T18:46:13Z&st=2022-01-21T10:46:13Z&spr=https&sig=WoerwGe%2BAc%2BkOU5hTzAMY2qZa8YE8Y3ayh8cFCfgtIE%3D" --share-name test.png