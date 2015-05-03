package com.example.servicetest;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends Activity {

	private TextView diesel, e20, e85, s91, s95, h91, h95, ngv, hpd;
	private String Diesel, E20, E85, S91, S95, H91, H95, NGV, HPD;
	
	@SuppressWarnings("unused")
	private boolean set = false;
	
	private static String URL = "http://www.pttplc.com/webservice/pttinfo.asmx?WSDL";
	private static String NAMESPACE = "http://www.pttplc.com/ptt_webservice/";
	private static String METHOD_NAME = "CurrentOilPrice";
	private static String SOAP_ACTION = "http://www.pttplc.com/ptt_webservice/CurrentOilPrice";

	private SoapPrimitive Results = null;
	HandleService sv = new HandleService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		diesel = (TextView) findViewById(R.id.txt1);
		e20 = (TextView) findViewById(R.id.txt2);
		e85 = (TextView) findViewById(R.id.txt3);
		s91 = (TextView) findViewById(R.id.txt4);
		s95 = (TextView) findViewById(R.id.txt5);
		h91 = (TextView) findViewById(R.id.txt6);
		h95 = (TextView) findViewById(R.id.txt7);
		ngv = (TextView) findViewById(R.id.txt8);
		hpd = (TextView) findViewById(R.id.txt9);
		
		Log.d("null","Call ConnectPtt");
		ConnectPtt();
		Log.d("null","Out ConnectPtt");
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setCancelable(true)
				.setTitle("Click")
				.setCancelable(true)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								diesel.setText(Diesel);
								e20.setText(E20);
								e85.setText(E85);
								s91.setText(S91);
								s95.setText(S95);
								h91.setText(H91);
								h95.setText(H95);
								ngv.setText(NGV);
								hpd.setText(HPD);
								Log.d("null","Set Text");
							}
						});
		
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	public void ConnectPtt() {
		Thread s = new Thread() {
			@Override
			public void run() {

				try {
					Log.d("null","In ConnectPtt");
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("Language", "EN");

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);

					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							URL);
					androidHttpTransport.call(SOAP_ACTION, envelope);
					SoapPrimitive resultRequestSOAP = (SoapPrimitive) envelope
							.getResponse();
					Results = resultRequestSOAP;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Log.d("null","Call parser");
					sv.pareser(Results);
					Log.d("null","Call set output");
					SetOutput();
				} catch (XmlPullParserException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		s.start();
	}
	
	private void SetOutput() {
		// TODO Auto-generated method stub
		
		try{
			Diesel = sv.getDiesel();
			E20 = sv.getGasoholE20();
			E85 = sv.getGasoholE85();
			S91 = sv.getGasoline91();
			S95 = sv.getGasoline95();
			H91 = sv.getGasohol91();
			H95 = sv.getGasohol95();
			NGV = sv.getNGV();
			HPD = sv.getHyForcePremiumDiesel();
			Log.d("null","Set output complete");
		}catch (Exception e) {
			e.printStackTrace();
			Log.d("null","Set output fail");
		}
		

	}

}
