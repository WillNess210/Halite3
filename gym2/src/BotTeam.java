public class BotTeam{
	public int numBots;
	public Bot[] bots;
	public BotTeam(Bot a){
		bots = new Bot[1];
		bots[0] = a;
		this.init();
	}
	public BotTeam(Bot a, Bot b){
		bots = new Bot[2];
		bots[0] = a;
		bots[1] = b;
		this.init();
	}
	public BotTeam(Bot a, Bot b, Bot c, Bot d){
		bots = new Bot[4];
		bots[0] = a;
		bots[1] = b;
		bots[2] = c;
		bots[3] = d;
		this.init();
	}
	public void init(){
		this.numBots = this.bots.length;
		for(int i = 0; i < numBots; i++){
			bots[i].id = i;
		}
	}
	public Bot[] getBots(){
		return this.bots;
	}
	public String getRunString(){
		String toReturn = "";
		for(Bot bot : this.bots){
			if(toReturn.length() > 0)
				toReturn += " ";
			toReturn += bot.getRunString();
		}
		return toReturn;
	}
}
