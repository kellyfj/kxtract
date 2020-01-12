package com.kxtract.transcription;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranscriberIntegrationTest {
	private static Logger logger = LoggerFactory.getLogger(TranscriberIntegrationTest.class);
	
	@Disabled
	@Test
	public void testDownload() throws MalformedURLException, IOException {
		String fileURI = "https://s3.amazonaws.com/aws-transcribe-us-east-1-prod/976421551064/myJob-2231597-on-the-grass-gareth-byres-on-grassroots-and-working-with-parents.mp3-1576185961216/83fb5cc2-bd90-4511-9a4b-84b81b87394a/asrOutput.json?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEMX%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCXVzLWVhc3QtMSJGMEQCICrbb0utShJCItClWvTktUMZ7VO9Fl%2BthFlm0cB407JkAiB370ngH%2BixBSDWTdTFU3NReRWNSCG3Vdt0hp3likkw9CrPAggdEAEaDDI3NjY1NjQzMzE1MyIMbHheV0tAriEXazGFKqwCZ2SewvsjqUsnFXI%2B0TTfhSEt1Ewypb9bEm67BZHOss1sY7iPzrkWMwBuj06%2FI82YDegMMwVdoApqvP5zHwiXxMcmo4I4cgPEW5wLarenNEFw0W6Uu6cvbWRf6MA7m%2FTt8%2F9wA6gjd7UjcIpP6Dsi92b3BzGUmx9xKx99Lv4UWRaDvSt8MX3PULMBnaCwqY%2BGWTr9BCDyKvoYSazQzqd0j38quHaSt6mKHSbzo5dfyL%2BNNzNo%2FmdUaagshYR5klAaK7mcbLsiwsiX8YIwZQtMTw3sLs3m9bxIM91PncsPguC5BhhHcJqh32GhUeaP%2B2TZkB22qVGIIsW%2Bs%2FO%2FdDCHQiwddCLXF3q3876oWkXBdaRWNmhxT3nya722v7HycEAoVfcqocgkUEfm0Y0UMLi%2Byu8FOtECTPbJtEM%2FacdouLHRxOHtkSP6AQC5QNnYCYaSXDZIegXjL8C%2BaTtqT8xhjF%2FtnrDGJUmdmi2qgNrtr9qu8d3pVhYhfkIeYYb%2FljVN3D16IE2gU3JC%2BcUerL8NPIPygsAN8g2CWS5LCUYbEN0k9gHK%2FJoqoajQaD53SCuij%2BA4AtbnCTjDS3IwijodrF1lTabtAHY0gja%2Bx%2B%2BoN%2FPY38anhtrCGZWNIq0iIUhsvMZDgbUJLhEZLE0RL3QVW9yYtWS1O1E0Jb%2B6%2FY%2Bgio8a1taTksV5CF3oPFRoStItrlUxZlNLnED%2BcNZQxTIZVNQF1OSTCbbeWCgY1rgzamCHtbPK%2BIUAZQN4dnMozlhRhFHu9YReWNnjvlUbq8zHuGBjX7KsZrBvmTj4tb6HDHKT%2Blq3iBkMyESCf46DabrMeipiMyeoyyx%2FsuLkGkTa3UyLrxO3Nw%3D%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20191212T213841Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=ASIAUA2QCFAAY4DSU6WG%2F20191212%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=b5b3c5bac7043873a68ee6c63335a385c6922b4d0a1781ce738fdb2535e37385";
		File targetFile = new File("/tmp/downloads/text_transcript.json");

		logger.info("Creating file " + targetFile.getAbsolutePath() + " . . . .");
		FileUtils.copyURLToFile(new URL(fileURI), targetFile);
		logger.info("File download Completed!");
	}
	
	@Test
	public void testSimplifyJSON() throws URISyntaxException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource("test_transcript.json").getFile());
		StringBuilder sb = Transcriber.simplifyTranscription(f.getAbsolutePath());
		Transcriber.writeStringToFile(sb, "/tmp/testSimplifyJSON_formatted.txt");
	}
}
