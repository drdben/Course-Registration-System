import java.util.*;

public abstract class User {
    Scanner sc = new Scanner(System.in);

    private String Email="";
   private String Password="";
   protected boolean Logged;

   public User(){
       this.Logged=false;
   }
//   public abstract void addspecs();
    public void login(){
       System.out.println("Enter Password: ");
       String pw = sc.nextLine();
//        System.out.println("Password is: "+this.getPassword());
        if(pw.equals(this.getPassword())){
            System.out.println("Login successful!\n");
            this.Logged=true;
        }
        else{
            this.Logged=false;
            System.out.println("Wrong Password!");
        }
    }

   public boolean getLogged(){
       return Logged;
   }

    protected String getEmail(){
       return Email;
   }
    protected void setEmail(String id){
        this.Email=id;
    }

   protected String getPassword(){
       return Password;
   }
   protected void setPassword(String pw){
       this.Password =pw;
   }
   protected void setLogged(boolean bool){
       this.Logged=bool;
   }
}
