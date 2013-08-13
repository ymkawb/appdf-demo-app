package util

import org.onepf.appdf.parser._
import play.Logger
object IOUtil {

	lazy val logger : Logger.ALogger = Logger.of("IOUtil")
	
    def inputToFile(is: java.io.InputStream, f: java.io.File) {
      val out = new java.io.FileOutputStream(f)
      try {
        val buff: Array[Byte] = new Array[Byte](1024 * 4)
        var count: Int = 0;
        var flag: Boolean = false

        while (!flag) {
          count = is.read(buff)
          if (count != -1)
            out.write(buff, 0, count)
          else
            flag = true
        }
      } finally {
        is.close()
        out.close
      }
    }
    
    
    def writeEntryToFileIfPresent(entry:String,parseResult:ParseResult,target:java.io.File) = {
      if ( entry != null ){
    	  val entryStream = parseResult.getEntryStream(entry)
    	  if ( !target.getParentFile().exists()){
    	    target.getParentFile.mkdirs()
    	  }
    	  if ( entryStream != null ){
    	    inputToFile(entryStream,target)
    	  }
      }      
    }
        
    def writeEntryToFileIfPresent[T](entries:java.util.Collection[T],toFileName : T => String,parseResult : ParseResult,targetDir:java.io.File,naming: String => String = x => x) {
      import scala.collection.JavaConversions._
      for( entry <- entries ){
        val fileName = toFileName(entry)
        val targetFile = new java.io.File(targetDir,naming(fileName))
        writeEntryToFileIfPresent(fileName, parseResult, targetFile)
      }
    }
    import java.io._
    def writeFileToByteStream(f:File) : InputStream = {
      val size : Int = f.length().asInstanceOf[Int]
      Logger.info("Size=" + size)
      val buff : Array[Byte] = new Array[Byte](size)
      var count : Int = 0
      var offset : Int = 0
      var flag : Boolean = true
      val fis : FileInputStream = new FileInputStream(f)
      while( flag ){
        count = fis.read(buff, offset, size - offset)        
        flag = count < 0 && size != offset
        offset = offset + count
      }
      new ByteArrayInputStream(buff)
    }
    
}