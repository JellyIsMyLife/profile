using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using MySql.Data.MySqlClient;
using LD2_Filmai.ViewModels;

namespace LD2_Filmai.Repos
{
    public class GarsoTakelisRepository
    {
        public List<GarsoTakelisViewModel> getGarsoTakeliai()
        {
            List<GarsoTakelisViewModel> garsoTakeliai = new List<GarsoTakelisViewModel>();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"SELECT m.id, m.atlikejas, m.pavadinimas, m.isleidimo_metai, mm.pavadinimas AS filmas
                                FROM garso_takeliai m
                                LEFT JOIN filmai mm ON mm.id=m.fk_filmas";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                garsoTakeliai.Add(new GarsoTakelisViewModel
                {
                    id = Convert.ToInt32(item["id"]),
                    atlikejas = Convert.ToString(item["atlikejas"]),
                    pavadinimas = Convert.ToString(item["pavadinimas"]),
                    isleidimoMetai = Convert.ToInt32(item["isleidimo_metai"]),
                    filmas = Convert.ToString(item["filmas"])
                });
            }

            return garsoTakeliai;
        }

        public GarsoTakelisEditViewModel getGarsoTakelis(int id)
        {
            GarsoTakelisEditViewModel garsoTakelis = new GarsoTakelisEditViewModel();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"SELECT m.* 
                                FROM garso_takeliai m WHERE m.id=" + id;
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                garsoTakelis.id = Convert.ToInt32(item["id"]);
                garsoTakelis.atlikejas = Convert.ToString(item["atlikejas"]);
                garsoTakelis.pavadinimas = Convert.ToString(item["pavadinimas"]);
                garsoTakelis.isleidimoMetai = Convert.ToInt32(item["isleidimo_metai"]);
                garsoTakelis.albumas = Convert.ToString(item["albumas"]);
                garsoTakelis.zanras = Convert.ToString(item["zanras"]);
                garsoTakelis.fk_filmas = Convert.ToInt32(item["fk_filmas"]);
            }

            return garsoTakelis;
        }

        public bool addGarsoTakelis(GarsoTakelisEditViewModel garsoTakelis)
        {
            try
            {
                string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
                MySqlConnection mySqlConnection = new MySqlConnection(conn);
                string sqlquery = @"INSERT INTO garso_takeliai(id,atlikejas,pavadinimas,isleidimo_metai,albumas,zanras,fk_filmas)
                                    VALUES(?id,?atlikejas,?pavadinimas,?isleidimo_metai,?albumas,?zanras,?filmas)";
                MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
                mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = garsoTakelis.id;
                mySqlCommand.Parameters.Add("?atlikejas", MySqlDbType.VarChar).Value = garsoTakelis.atlikejas;
                mySqlCommand.Parameters.Add("?pavadinimas", MySqlDbType.VarChar).Value = garsoTakelis.pavadinimas;
                mySqlCommand.Parameters.Add("?isleidimo_metai", MySqlDbType.Int32).Value = garsoTakelis.isleidimoMetai;
                mySqlCommand.Parameters.Add("?albumas", MySqlDbType.VarChar).Value = garsoTakelis.albumas;
                mySqlCommand.Parameters.Add("?zanras", MySqlDbType.VarChar).Value = garsoTakelis.zanras;
                mySqlCommand.Parameters.Add("?filmas", MySqlDbType.VarChar).Value = garsoTakelis.fk_filmas;
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

        public bool updateGarsoTakelis(GarsoTakelisEditViewModel garsoTakelis)
        {
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"UPDATE garso_takeliai a 
                                SET a.atlikejas=?atlikejas,
                                    a.pavadinimas=?pavadinimas,
                                    a.isleidimo_metai=?isleidimo_metai,
                                    a.albumas=?albumas,
                                    a.zanras=?zanras,
                                    a.fk_filmas=?filmas 
                                    WHERE a.id=?id";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = garsoTakelis.id;
            mySqlCommand.Parameters.Add("?atlikejas", MySqlDbType.VarChar).Value = garsoTakelis.atlikejas;
            mySqlCommand.Parameters.Add("?pavadinimas", MySqlDbType.VarChar).Value = garsoTakelis.pavadinimas;
            mySqlCommand.Parameters.Add("?isleidimo_metai", MySqlDbType.Int32).Value = garsoTakelis.isleidimoMetai;
            mySqlCommand.Parameters.Add("?albumas", MySqlDbType.VarChar).Value = garsoTakelis.albumas;
            mySqlCommand.Parameters.Add("?zanras", MySqlDbType.VarChar).Value = garsoTakelis.zanras;
            mySqlCommand.Parameters.Add("?filmas", MySqlDbType.VarChar).Value = garsoTakelis.fk_filmas;
            mySqlConnection.Open();
            mySqlCommand.ExecuteNonQuery();
            mySqlConnection.Close();
            return true;
        }

        public void deleteGarsoTakelis(int id)
        {
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"DELETE FROM garso_takeliai where id=?id";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = id;
            mySqlConnection.Open();
            mySqlCommand.ExecuteNonQuery();
            mySqlConnection.Close();
        }
    }
}