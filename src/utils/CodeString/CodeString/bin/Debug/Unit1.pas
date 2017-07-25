unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,ComObj,
  Dialogs, StdCtrls;

type
  TForm1 = class(TForm)
    Test: TButton;
    procedure TestClick(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  Form1: TForm1;

implementation

uses codeString_TLB;

{$R *.dfm}

procedure TForm1.TestClick(Sender: TObject);
var
  CodeSt:clsCodeStringInterface;
  Mac,Ws ,Pincode    ,strMacKey :WideString;

begin
  Pincode :='123456';
  strMacKey := '1C1C1C1C1C1C1C1C1C1C';
  Ws := '1200F634840188E110000100000004800011'
        +'#0'+'91000000000000000000000000000002251659056318571402251659050000022561050061317C'
        +'#6'+'628023'#6'62802300000063185709999402000009999402   '
        +'#7'+'TelBank'+'#0#0'+'6D6D98E945BC9EB6'+'#0#0#$11'+'13000000086'+'#0'
        +'RSignedBy=0062756826:2bcf35bc3c7c2cf2040558e620a8a042?I?xmo';
  CodeSt  :=  CreateComObject(CLASS_clsCodeString) as clsCodeStringInterface;
  Mac:=CodeSt.codeString(Ws,strMacKey);//  Mac  S128 AN16
  ShowMessage(Mac);
end;

end.
