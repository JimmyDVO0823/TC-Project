/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AutomataPila;

/**
 *
 * @author Kevin
 */
public class Termino {

    String valor;
    String valor2;
    boolean signo;
    boolean producto;
    boolean division;

    public Termino(String valor, String valor2, boolean signo, boolean producto, boolean division) {
        this.valor = valor;
        this.valor2 = valor2;
        this.signo = signo;
        this.producto = producto;
        this.division = division;
        arreglarTermino();
    }
    
    public final void arreglarTermino(){
        if(esNumerico(valor) && esNumerico(valor2)){
            if(producto) {
                valor = String.valueOf(Integer.parseInt(valor)*Integer.parseInt(valor2));
            } else {
                valor = String.valueOf(Integer.parseInt(valor)*Integer.parseInt(valor2));
            }
            valor2 = "";
            producto = false;
            division = false;
        }
    }
    
    public boolean esNumerico(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public boolean isSigno() {
        return signo;
    }

    public void setSigno(boolean signo) {
        this.signo = signo;
    }

    public boolean isProducto() {
        return producto;
    }

    public void setProducto(boolean producto) {
        this.producto = producto;
    }

    public boolean isDivision() {
        return division;
    }

    public void setDivision(boolean division) {
        this.division = division;
    }

    @Override
    public String toString() {
        String signoStr = signo ? "+" : "-";
        String operador = "";
        if (producto) {
            operador = "*";
        } else if (division) {
            operador = "/";
        }

        if (!valor2.isEmpty()) {
            return signoStr + valor + operador + valor2;
        } else {
            return signoStr + valor;
        }
    }
}
