import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import jAudioFeatureExtractor.AudioFeatures.MFCC;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;

public class FeatureExtraction {
	
	public static void main(String[] args){
		
		final double SAMPLE_DURATION = 10.0d;
		
		if( args.length == 1 ){
			System.err.println( "Please supply the local path to audio files to train on" );
			System.exit(1);
		}
		// filter non .wav files
		
		//AudioSamples[] audioSamples = new AudioSamples[args.length - 1];
		for( int i = 1; i < args.length; i++ ){
			String uid = args[i].substring(0, args[i].length()-4);
			File inputFile = new File(args[i]);
			AudioSamples audioSample;
			try{
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(inputFile);
				audioSample = new AudioSamples(inputStream, uid, false);
			} catch(Exception e){
				audioSample = null;
				System.out.println(e);
				System.exit(2);
			}
			
			double duration = audioSample.getDuration();
			double currStart = 0.0d;
			while( currStart < duration ){
				double currEnd;
				if( duration-currStart < 2*SAMPLE_DURATION){
					currEnd = duration;
				} else {
					currEnd = currStart + SAMPLE_DURATION;
				}
				
				double[] samples;
				try{
					samples = audioSample.getSamplesMixedDown(currStart, currEnd);
				} catch(Exception e){
					samples = null;
					System.out.println(e);
					System.exit(3);
				}
				
				MFCC mfcc = new MFCC();
				for( String dependence : mfcc.getDepenedencies()){
					System.out.println( dependence );
				}
				System.out.println("boop");
				
				currStart += SAMPLE_DURATION;
			}
		}
		
		
		
	}
	
}