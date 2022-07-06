package solidappservice.cm.com.presenteapp.adapters.transferencias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

public class AccountsAdapter extends ArrayAdapter<ResponseCuentasInscritas> {

    private ActivityBase context;
    private List<ResponseCuentasInscritas> cuentasInscritas;

    public AccountsAdapter(ActivityBase context, List<ResponseCuentasInscritas> cuentasInscritas){
        super(context, R.layout.list_item_cuentas, cuentasInscritas);
        this.context = context;
        this.cuentasInscritas = cuentasInscritas;
    }

    @Override
    public ResponseCuentasInscritas getItem(int position) {
        return cuentasInscritas.get(position);
    }

    @Override
    public int getCount() {
        return (cuentasInscritas == null ? 0 : cuentasInscritas.size());
    }

    @Override
    public @NonNull View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(view == null){
            view = inflater.inflate(R.layout.list_item_cuentas, parent, false);
        }

        TextView lblPropietarioCuenta = view.findViewById(R.id.lblPropietarioCuenta);
        TextView lblNumeroCuenta = view.findViewById(R.id.lblNumeroCuenta);
        CheckBox chkItem = view.findViewById(R.id.item_checkbox);

        ResponseCuentasInscritas cuentaInscrita = cuentasInscritas.get(position);
        lblPropietarioCuenta.setText(cuentaInscrita.getNnasocia());
        lblNumeroCuenta.setText(cuentaInscrita.getN_numcta());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cuentasInscritas != null) {
                    ResponseCuentasInscritas cuentaSelected = cuentasInscritas.get(position);

                }
            }
        });

        return view;
    }
}
