package com.ftec.kidapp.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftec.kidapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {

    private List<HistoricoItem> historicoItemList;

    public HistoricoAdapter(List<HistoricoItem> historicoItemList) {
        this.historicoItemList = historicoItemList;
    }

    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historico, parent, false);
        return new HistoricoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        HistoricoItem historicoItem = historicoItemList.get(position);
        holder.bindData(historicoItem);
    }

    @Override
    public int getItemCount() {
        return historicoItemList.size();
    }

    public class HistoricoViewHolder extends RecyclerView.ViewHolder {
        TextView buttonNameTextView;
        TextView dateTimeTextView;

        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonNameTextView = itemView.findViewById(R.id.buttonNameTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
        }

        public void bindData(HistoricoItem historicoItem) {
            String buttonName = historicoItem.getButtonName();
            String dateTime = historicoItem.getDateTime();

            // Separar a data e hora
            String[] parts = dateTime.split(" ");
            String date = parts[0];
            String time = parts[1];

            // Separar o dia e mês
            String[] dateParts = date.split("-");
            String day = dateParts[2];
            String month = dateParts[1];

            // Verificar se é o jogo da memória e ajustar o nome
            if (buttonName.equals("btnJogoMemo")) {
                buttonName = "Jogo da Memória";
            } else if (buttonName.equals("btnHistoria")) {
                buttonName = "História os 3 porquinhos";

            }
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                Date parsedDate = inputFormat.parse(time);
                time = outputFormat.format(parsedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Exibir o nome da história, dia, mês e hora
            buttonNameTextView.setText(buttonName);
            dateTimeTextView.setText("Ultimo acesso às "+ time + " no dia " + day + "/" + month + " " );
        }
    }
}
