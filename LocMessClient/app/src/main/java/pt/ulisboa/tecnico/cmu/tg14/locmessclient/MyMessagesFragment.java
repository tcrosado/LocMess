package pt.ulisboa.tecnico.cmu.tg14.locmessclient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Message;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.ServicesDataHolder;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MessageNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.MultipleRowsAfectedException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions.PublisherNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Services.DBService;
import pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.FeedReaderDbHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMessagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<String> messagesList;
    private HashMap<String, String> hashAux;
    private ArrayAdapter<String> arrayAdapter;

    private ListView listView;

    public MyMessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyMessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMessagesFragment newInstance(String param1, String param2) {
        MyMessagesFragment fragment = new MyMessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.fragment_my_messages_title);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMessage.class);
                startActivity(intent);
            }});
    }

    @Override
    public void onResume() {
        super.onResume();

        //Log.d("MyMessages: ", "on resume" );

        new GetMessagesFromDatabaseTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.fragment_my_messages_title);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_messages, container, false);
        messagesList = new ArrayList<>();
        hashAux = new HashMap<>();

        listView = (ListView) view.findViewById(R.id.list_my_messages_list);
        arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, messagesList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                Toast.makeText(view.getContext(), "Long press to delete", Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                final FeedReaderDbHelper dbHelper = FeedReaderDbHelper.getInstance(getActivity());
                final String messageContent = messagesList.get(position);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                String aux = getResources().getString(R.string.prompt_delete_key);

                alertDialogBuilder.setTitle(aux + ' ' + messageContent + '?');

                // set dialog message
                alertDialogBuilder
                        //.setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Toast.makeText(getActivity(),getString(R.string.message_removed),Toast.LENGTH_LONG).show();

                                try {
                                    dbHelper.deleteMessageInTheFuture(hashAux.get(messageContent));
                                } catch (MessageNotFoundException e) {
                                    e.printStackTrace();
                                } catch (MultipleRowsAfectedException e) {
                                    e.printStackTrace();
                                }

                                hashAux.remove(messageContent);
                                messagesList.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                return true;
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class GetMessagesFromDatabaseTask extends AsyncTask<Void, Void, Void> {

        List<String> list2update;

        public GetMessagesFromDatabaseTask() {
            list2update = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, messagesList);
            listView.setAdapter(arrayAdapter);
            //Log.d("MyMessages: ", "onPreExecute");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            messagesList.clear();

            for(String s : list2update){
                messagesList.add(s);
            }
            arrayAdapter.notifyDataSetChanged();
            //Log.d("MyMessages: ", "onPostExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            FeedReaderDbHelper dbHelper = FeedReaderDbHelper.getInstance(getActivity());

            String username = ServicesDataHolder.getInstance().getUsername();

            List<Message> messagesDB;

            try {

                messagesDB = dbHelper.getMessagesFromUser(username);
                for(Message m: messagesDB){
                    list2update.add(m.getContent());
                    hashAux.put(m.getContent(),m.getUUID().toString());
                    //Log.d("MyMessages: ", "received message ->" + m.getUUID());
                }

                //Log.d("MyMessages: ", "received messages ->" +list2update.size() );
            } catch (PublisherNotFoundException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}
