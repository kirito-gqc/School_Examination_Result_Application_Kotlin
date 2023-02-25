package com.example.examresultenteringsystem

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudentAdapter (val context: Context, val studentList: ArrayList<Student>):
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private lateinit var mDbRef: DatabaseReference

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textName: TextView
        var textClass: TextView
        var texttest1: TextView
        var texttest2: TextView
        var sMenu: ImageView


        init {
            textName = itemView.findViewById<TextView>(R.id.Text_student_name)
            textClass = itemView.findViewById<TextView>(R.id.Text_class_name)
            texttest1 = itemView.findViewById<TextView>(R.id.Text_test_1)
            texttest2 = itemView.findViewById<TextView>(R.id.Text_test_2)
            sMenu = itemView.findViewById(R.id.studentMenu)
            sMenu.setOnClickListener { popupMenus(it) }
        }

        private fun popupMenus(itemView: View) {
            val position = studentList[adapterPosition]
            val popupMenus = PopupMenu(context, itemView)
            popupMenus.inflate(R.menu.student_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editStudentButton -> {
                        val v = LayoutInflater.from(context).inflate(R.layout.edit_student_item, null)

                        val studentname = v.findViewById<TextView>(R.id.Text_student_name)

                        val classname = v.findViewById<EditText>(R.id.Text_class_name)
                        val test1 = v.findViewById<EditText>(R.id.Text_test_1)
                        val test2 = v.findViewById<EditText>(R.id.Text_test_2)

                        classname.setText(position.classname)
                        studentname.setText(position.studentname)

                        AlertDialog.Builder(context)
                            .setView(v)
                            .setPositiveButton("Ok") { dialog, _ ->


                                val newclass = classname.text.toString()
                                val newtest1 = test1.text.toString()
                                val newtest2 = test2.text.toString()
                                val status = "Available"
                                position.classname = newclass
                                position.test1 = newtest1
                                position.test2 = newtest2
                                notifyDataSetChanged()

                                updatedata(studentname.text.toString(),newclass, newtest1, newtest2,status)

                                dialog.dismiss()

                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.RemoveButton -> {
                        AlertDialog.Builder(context)
                            .setTitle("Remove Student")
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .setMessage("Are you sure to remove this student Information")
                            .setPositiveButton("Yes") { dialog, _ ->
                                studentList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(
                                    context,
                                    "Student record removed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                                val studentname = position.studentname.toString()
                                val newstatus = "Removed"
                                removedata(studentname,newstatus)
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }
        private fun removedata(studentname: String,newstatus: String) {
            mDbRef = FirebaseDatabase.getInstance().getReference("Student")
            val removestudent = mapOf<String,String>(
                "status" to newstatus
            )

            mDbRef.child(studentname).updateChildren(removestudent).addOnSuccessListener {
                Toast.makeText(context,"Student Record Removed", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener{
            Toast.makeText(context,"Student Record Fail to Remove", Toast.LENGTH_SHORT).show()
        }
}

        private fun updatedata(studentname:String,newclass: String, newtest1: String, newtest2: String, status: String) {

            mDbRef = FirebaseDatabase.getInstance().getReference("Student")
            val editstudent = mapOf<String, String>(
                "classname" to newclass,
                "test1" to newtest1,
                "test2" to newtest2,
                "status" to status
            )

            mDbRef.child(studentname).updateChildren(editstudent).addOnSuccessListener {
                Toast.makeText(context, "Student Information and Result Edited", Toast.LENGTH_SHORT)
                    .show()
            }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Student Information and Result Fail to Edit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.student_item, parent, false)
            return StudentViewHolder(view)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            val currentStudent = studentList[position]

            holder.textName.text = currentStudent.studentname
            holder.textClass.text = currentStudent.classname
            holder.texttest1.text = currentStudent.test1
            holder.texttest2.text = currentStudent.test2
        }

        override fun getItemCount(): Int {
            return studentList.size
        }



}

