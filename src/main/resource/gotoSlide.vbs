'String path ="gotoSlide.vbs "+slideNumber;

'Runtime rt = Runtime.getRuntime();

'Process p = rt.exec(new String[] {"cmd.exe", "/c", path});

Dim objPPT
Dim oArgs

Dim slideIndex 
Dim slideNumber


Set oArgs = WScript.Arguments



Set objPPT = GetObject(, "PowerPoint.Application")



' Did we find anything...?

If Not TypeName(objPPT) = "Empty" Then

On Error Resume Next
    'objPPT.SlideShowWindows(1).View.GotoSlide oArgs(0)
	 slideIndex = objPPT.SlideShowWindows(1).View.Slide.SlideIndex
	 slideNumber = objPPT.SlideShowWindows(1).View.Slide.SlideNumber
	
	 slideIndex = slideIndex+oArgs(0)
	objPPT.SlideShowWindows(1).View.GotoSlide slideIndex 
	
End If
