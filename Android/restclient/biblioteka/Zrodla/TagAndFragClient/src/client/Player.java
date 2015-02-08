package client;

public class Player

{

   private String name;

   private Integer health;

   private Integer ammunition;

   private Integer localization;

 

   public Player(){

 

   }



   public Player(String name, Integer health, Integer ammunition, Integer localization){

   this.name = name;

   this.health = health;

   this.ammunition = ammunition;

   this.localization = localization;

   }

   public String getName()

   {

      return name;

   }

   public void setName(String name)

   {

      this.name = name;

   } 

   public Integer getHealth()

   {

      return health;

   }

   public void setHealth(Integer health)

   {

      this.health = health;

   }

   public Integer getAmmunition()

   {

      return ammunition;

   }

   public void setAmmunition(Integer ammunition)

   {

      this.ammunition = ammunition;

   }

   public Integer getLocalization()

   {

      return localization;

   }

   public void setLocalization(Integer localization)

   {

      this.localization = localization;

   }

 

   @Override

   public String toString()

   {

      return "[name=" + name + ", health=" + health + ", " +

            "ammunition=" + ammunition + " localization=" + localization +"]";

   }

}