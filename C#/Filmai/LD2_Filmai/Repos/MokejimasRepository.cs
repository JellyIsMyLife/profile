using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using MySql.Data.MySqlClient;
using LD2_Filmai.Models;

namespace LD2_Filmai.Repos
{
    public class MokejimasRepository
    {
        public List<Mokejimas> getMokejimai()
        {
            List<Mokejimas> mokejimai = new List<Mokejimas>();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"SELECT * from mokejimai";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                mokejimai.Add(new Mokejimas
                {
                    id = Convert.ToInt32(item["id"]),
                    data = Convert.ToDateTime(item["data"]),
                    suma = Convert.ToDouble(item["suma"]),
                    fk_klientas = Convert.ToInt32(item["fk_klientas"]),
                    fk_saskaita = Convert.ToInt32(item["fk_saskaita"])
                });
            }

            return mokejimai;
        }

        public Mokejimas getMokejimas(int id)
        {
            Mokejimas mokejimas = new Mokejimas();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"SELECT m.* 
                                FROM mokejimai m WHERE m.id=" + id;
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                mokejimas.id = Convert.ToInt32(item["id"]);
                mokejimas.data = Convert.ToDateTime(item["data"]);
                mokejimas.suma = Convert.ToDouble(item["suma"]);
                mokejimas.fk_klientas = Convert.ToInt32(item["fk_klientas"]);
                mokejimas.fk_saskaita = Convert.ToInt32(item["fk_saskaita"]);
            }

            return mokejimas;
        }

        public bool addMokejimas(Mokejimas mokejimas)
        {
            try
            {
                string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
                MySqlConnection mySqlConnection = new MySqlConnection(conn);
                string sqlquery = @"INSERT INTO mokejimai(id,data,suma,fk_klientas,fk_saskaita)
                                    VALUES(?id,?data,?suma,?klientas,?saskaita)";
                MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
                mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = mokejimas.id;
                mySqlCommand.Parameters.Add("?data", MySqlDbType.DateTime).Value = mokejimas.data;
                mySqlCommand.Parameters.Add("?suma", MySqlDbType.Double).Value = mokejimas.suma;
                mySqlCommand.Parameters.Add("?klientas", MySqlDbType.Int32).Value = mokejimas.fk_klientas;
                mySqlCommand.Parameters.Add("?saskaita", MySqlDbType.Int32).Value = mokejimas.fk_saskaita;
                mySqlConnection.Open();
                mySqlCommand.ExecuteNonQuery();
                mySqlConnection.Close();

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public bool updateMokejimas(Mokejimas mokejimas)
        {
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"UPDATE mokejimai a 
                                SET a.data=?data,
                                    a.suma=?suma,
                                    a.fk_klientas=?klientas,
                                    a.fk_saskaita=?saskaita
                                    WHERE a.id=?id";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = mokejimas.id;
            mySqlCommand.Parameters.Add("?data", MySqlDbType.DateTime).Value = mokejimas.data;
            mySqlCommand.Parameters.Add("?suma", MySqlDbType.Double).Value = mokejimas.suma;
            mySqlCommand.Parameters.Add("?klientas", MySqlDbType.Int32).Value = mokejimas.fk_klientas;
            mySqlCommand.Parameters.Add("?saskaita", MySqlDbType.Int32).Value = mokejimas.fk_saskaita;
            mySqlConnection.Open();
            mySqlCommand.ExecuteNonQuery();
            mySqlConnection.Close();
            return true;
        }

        public void deleteMokejimas(int id)
        {
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"DELETE FROM mokejimai where id=?id";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = id;
            mySqlConnection.Open();
            mySqlCommand.ExecuteNonQuery();
            mySqlConnection.Close();
        }
    }
}