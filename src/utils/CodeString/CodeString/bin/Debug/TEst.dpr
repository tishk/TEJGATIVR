program TEst;

uses
  Forms,
  Unit1 in 'Unit1.pas' {Form1},
  codeString_TLB in '..\..\Program Files (x86)\Borland\Delphi7\Imports\codeString_TLB.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.CreateForm(TForm1, Form1);
  Application.Run;
end.
