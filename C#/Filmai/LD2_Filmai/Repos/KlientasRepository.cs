using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using MySql.Data.MySqlClient;
using LD2_Filmai.Models;

namespace LD2_Filmai.Repos
{
    public class KlientasRepository
    {
        public List<Klientas> getKlientai()
        {
            List<Klientas> klientai = new List<Klientas>();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"SELECT * from klientai";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                klientai.Add(new Klientas
                {
                    asmensKodas = Convert.ToInt32(item["asmens_kodas"]),
                    vardas = Convert.ToString(item["vardas"]),
                    pavarde = Convert.ToString(item["pavarde"]),
                    gimimoData = Convert.ToDateTime(item["gimimo_data"]),
                    telefonas = Convert.ToString(item["telefonas"]),
                    elPastas = Convert.ToString(item["el_pastas"]),
                });
            }

            return klientai;
        }
    }
}