package jk.mkv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

public class SRTConverter {
	
  public static void main(String[] args) throws Exception
  {
	  switch(args.length)
	  {
		  case 0:
			  System.out.println("No input file specified");
			  return;
		  case 1:
			  System.out.println("No output file specified");
			  return;
		  case 2: //OK
			  break;
		  default:
			  System.out.println("Invalid number of arguments");
			  return;
	  }
	  
	  String inPath = args[0];
	  String outPath = args[1];
	  
	  if(!(new File(inPath)).exists())
		  System.out.println("File not found");
	  
	  if(checkEncoding(inPath, Charset.forName("UTF-8")))
		  System.out.println("Input file detected as UTF-8");
	  else
		  System.out.println("Input file detected as ISO-8859-1");
	  
	  System.out.println("System default charset is " + Charset.defaultCharset().name());
	  
	  convertToDefault(inPath, outPath);
	  //convert(inPath, outPath, Charset.forName("windows-1252"));

	  if(checkEncoding(outPath, Charset.forName("UTF-8")))
		  System.out.println("Output file detected as UTF-8");
	  else
		  System.out.println("Output file detected as ISO-8859-1");
  }

  public static void convert(String input, String output, Charset destCharset) throws Exception
  {
	  Charset utf8 = Charset.forName("UTF-8");
	  if(checkEncoding(input, utf8))
		  convertToEncoding(input, output, utf8, destCharset);
	  else
		  convertToEncoding(input, output, Charset.forName("ISO-8859-1"), destCharset);
  }
	
  public static void convertToDefault(String input, String output) throws Exception
  {
	  convert(input, output, Charset.defaultCharset());
  }
  
  private static void convertToEncoding(String input, String output, Charset origCharset, Charset destCharset) throws Exception
  {
	  CharsetEncoder encoder = destCharset.newEncoder();
	  encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
	  
	  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input), origCharset));
	  BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), destCharset));
	  
	  String line = null;
	  while((line = br.readLine()) != null)
	  {
		  if(line.length() > 1 && (((int)line.charAt(0)) == 65279)) //remove UTF BOM
			  line = line.substring(1);
		  
		  //for(int i=0; i< line.length(); i++)
		  //	System.out.print(line.charAt(i) + "(" + ((int) line.charAt(i)) + ") ");
		  //System.out.println();
		  
		  String temp = destCharset.decode(encoder.encode(CharBuffer.wrap(line))).toString();
		  bw.write(temp);
		  bw.newLine();
	  }
	  br.close();
	  bw.close();
  }
  
  private static boolean checkEncoding(String path, Charset cs) throws Exception
  {
	  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), cs));
	  
	  String line = null;
	  boolean suspect = false;
	  int lastExt = -2;
	  
	  while((line = br.readLine()) != null)
	  {
		  lastExt = -2;
		  
		  for(int i=0; i<line.length(); i++)
		  {
			  if(line.charAt(i) > 127)
			  {
				  //System.out.println(i + ": " + ((char)line.charAt(i)) + " = " + ((int)line.charAt(i)));
  				  
				  if(((int)line.charAt(i)) == 65533) suspect = true; //read iso as utf
				  
				  if(((int)line.charAt(i)) == 195) lastExt = i;
				  if(lastExt == (i-1)) suspect = true; //read utf as iso
			  }
			  
			  if(suspect) break;
		  }
		  
		  if(suspect) break;
	  }
	  
	  br.close();
	  
	  return !suspect;
  }
}