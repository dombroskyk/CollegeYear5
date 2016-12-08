import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import jAudioFeatureExtractor.AudioFeatures.MFCC;
import jAudioFeatureExtractor.AudioFeatures.MagnitudeSpectrum;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;

public class FeatureExtraction {
	
	final static double SAMPLE_DURATION = 3.0d;
	
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		
		if( args.length == 0 ){
			System.err.println( "Please supply the local path to audio files to train on" );
			System.exit(1);
		}
		// filter non .wav files
		
		PrintWriter enWriter = new PrintWriter("en.csv", "UTF-8");
		PrintWriter esWriter = new PrintWriter("es.csv", "UTF-8");
		PrintWriter plWriter = new PrintWriter("pl.csv", "UTF-8");
		for( int i = 0; i < args.length; i++ ){
			String uid = args[i].substring(0, args[i].length()-4);
			String langAbbr = args[i].substring(0, 2);
			PrintWriter writer;
			switch(langAbbr){
				case "en":
					writer = enWriter;
					break;
				case "es":
					writer = esWriter;
					break;
				case "pl":
					writer = plWriter;
					break;
				default:
					writer = null;
					break;
			}
			File inputFile = new File(args[i]);
			AudioSamples audioSample;
			double[][] samples;
			try{
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(inputFile);
				audioSample = new AudioSamples(inputStream, uid, false);
				samples = audioSample.getSampleWindowsMixedDown(SAMPLE_DURATION);
			} catch(Exception e){
				audioSample = null;
				samples = null;
				e.printStackTrace();
				System.exit(2);
			}

			try{
				double sampleRate = audioSample.getSamplingRateAsDouble();
				for( double[] sample : samples ){
					MagnitudeSpectrum currMagSpec = new MagnitudeSpectrum();
					MFCC currMFCC = new MFCC();
					double[][] mfccMagSpecPass = new double[1][];
					mfccMagSpecPass[0] = currMagSpec.extractFeature(sample, sampleRate, null);
					double[] mfccSamples = currMFCC.extractFeature(sample, sampleRate, mfccMagSpecPass);
					
					StringBuilder result = new StringBuilder();
				    for(double mfcc : mfccSamples ){
				        result.append(Double.toString(mfcc) + ",");
				    }
				    writer.println(result);
				}
			}catch(Exception e){
				e.printStackTrace();
				System.exit(3);
			}
		}
		enWriter.close();
		esWriter.close();
		plWriter.close();
	}
	
}
