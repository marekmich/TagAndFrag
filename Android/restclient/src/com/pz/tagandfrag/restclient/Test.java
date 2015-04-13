package com.pz.tagandfrag.restclient;



public class Test  {

    Game gra;

    public Test(Game gra){
        this.gra = gra;
    }
    
    public void locChange(Player p, String s, Integer t) throws Exception
    {
    	p.setLocalization(s);
    	this.gra.updatePlayer(p);
    	Thread.sleep(t);
    	System.out.println("Localization change: "+p.getName());
    }

    public void shot(Player p, String q, Integer t) throws Exception
    {
    	this.gra.shotPlayer(p,q);
    	Thread.sleep(t);
    	System.out.println("Shot");
    }
    public void script1() {
        try{
		
		Player player1= gra.getByName("wojtek"); player1.setHealthPoints(100);
		Player player2= gra.getByName("luigi"); player2.setHealthPoints(100);
		Player player3= gra.getByName("marek"); player3.setHealthPoints(100);
		Player player4= gra.getByName("zdenek"); player4.setHealthPoints(100);

		
		locChange(player1, "53.009867#18.594519", 1000);
		
		locChange(player2, "53.010639#18.591866", 1000);
		
		locChange(player1, "53.009842#18.594755", 1000);
		
		locChange(player2, "53.010639#18.591523", 1000);
				
		locChange(player1, "53.009742#18.594958", 1000);
		
		locChange(player2, "53.010497#18.591190", 1000);
		
		locChange(player1, "53.009642#18.595162", 1000);
		
		locChange(player2, "53.010109#18.591158", 700);
		
		shot(player1, player4.getName(), 300);
		
		locChange(player2, "53.009858#18.591630", 1000);
		
		locChange(player1, "53.009542#18.595366", 1000);
		
		locChange(player2, "53.009490#18.591887", 1000);
		
		locChange(player1, "53.009461#18.595672", 1000);
		
		locChange(player2, "53.009186#18.592359", 1000);
		
		locChange(player1, "53.009461#18.596069", 1000);
		
		locChange(player2, "53.008986#18.593078", 1000);
		
		locChange(player1, "53.009658#18.596192", 1000);
		
		locChange(player2, "53.008805#18.593915", 1000);
		
		locChange(player1, "53.009913#18.596117", 1000);
		
		locChange(player2, "53.008786#18.594720", 1000);
		
		locChange(player1, "53.010048#18.595774", 1000);
		
		locChange(player2, "53.009006#18.594548", 1000);
		
		locChange(player1, "53.010132#18.595398", 1000);
		
		locChange(player2, "53.008960#18.595482", 1000);
		
		locChange(player1, "53.010355#18.595398", 1000);
		
		locChange(player2, "53.009483#18.595095", 1000);
		
		locChange(player1, "53.010658#18.595315", 2000);
		
		locChange(player1, "53.010671#18.594908", 2000);
				
		locChange(player1, "53.010652#18.594285", 2000);
	
		locChange(player1, "53.010626#18.593481", 2000);
			
		locChange(player1, "53.010600#18.592697", 2000);
			
		locChange(player1, "53.010619#18.592032", 2000);
		
		
		
		
        } catch(Exception e ) {} 
		
	}
    
    public void script2() {
        try{
		
		Player player1= gra.getByName("marek"); player1.setHealthPoints(100);
		Player player2= gra.getByName("zdenek"); player2.setHealthPoints(100);
		Player player3= gra.getByName("mateo"); player3.setHealthPoints(100);
		
		locChange(player1, "53.011756#18.594189", 1000);
		
		locChange(player2, "53.011607#18.598287", 1000);
		
		locChange(player1, "53.011756#18.594671", 1000);
		
		locChange(player2, "53.011787#18.597214", 1000);
				
		locChange(player3, "53.011807#18.595358", 1000);
		
		locChange(player2, "53.011891#18.595991", 1000);
		
		locChange(player1, "53.011562#18.595895", 1000);
		
		locChange(player2, "53.011787#18.594618", 200);
		
		shot(player1, player2.getName(), 800);
		
		locChange(player2, "53.011749#18.593438", 1000);
		
		locChange(player1, "53.011284#18.595723", 100);
		
		shot(player3, player2.getName(), 900);
		
		locChange(player2, "53.012562#18.593545", 150);
		
		shot(player1, player2.getName(), 850);
		
		locChange(player1, "53.010942#18.595122", 1000);
		
		locChange(player2, "53.013337#18.593760", 1000);
		
		locChange(player1, "53.010665#18.594382", 300);
		
		shot(player1, player2.getName(), 700);
		
		locChange(player2, "53.014059#18.593073", 1000);
		
		locChange(player1, "53.010510#18.593674", 100);
		
		shot(player1, player2.getName(), 900);
		
		locChange(player2, "53.014240#18.591056", 1000);
		
		locChange(player1, "53.010193#18.593577", 1000);
		
		locChange(player2, "53.014318#18.588674", 1000);
		
		locChange(player1, "53.009877#18.593513", 200);
		
		shot(player1, player2.getName(), 800);
		
		locChange(player2, "53.014150#18.589146", 100);
		
		shot(player3, player1.getName(), 900);
		
		locChange(player1, "53.009574#18.593277", 1000);
		
		locChange(player2, "53.014098#18.588696", 960);
		
		shot(player1, player2.getName(), 40);
		
		locChange(player1, "53.009406#18.594017", 1000);
		
		locChange(player2, "53.014150#18.589640", 1000);
		
		locChange(player1, "53.009445#18.594843", 1000);
		shot(player1, player2.getName(), 200);
		shot(player1, player3.getName(), 200);
		shot(player1, player2.getName(), 200);
		shot(player2, player1.getName(), 200);
		shot(player1, player2.getName(), 200);
		shot(player2, player1.getName(), 200);
		shot(player1, player2.getName(), 200);
		shot(player2, player1.getName(), 200);
		shot(player1, player2.getName(), 200);

		
		
		
		
        } catch(Exception e ) {} 
		
	}	
}
