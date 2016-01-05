[CmdletBinding()]
param(
	$path, $zipPath,$templateName
)

Write-Output "$path"

PUSHD "$path"

Write-Output $("Zipping to " + $zipPath)

function ZipFiles( $zipfilename, $sourcedir )
{
  if(Test-Path $zipfilename){
    Remove-Item $zipfilename;
  }
   Add-Type -Assembly System.IO.Compression.FileSystem;
   $compressionLevel = [System.IO.Compression.CompressionLevel]::Optimal;

   [System.IO.Compression.ZipFile]::CreateFromDirectory("$sourcedir","$zipfilename", $compressionLevel, $false);
}

ZipFiles $($zipPath+"\"+$templateName+".zip") "$path"

Write-Output "Zipped"

POPD