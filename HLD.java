
    import java.awt.HeadlessException;
import java.io.*;
    import java.math.*;
    import java.security.KeyStore.Entry;
    import java.util.*;

            
     class HLD
         
          {
        	
        	private InputStream is;
        	private  PrintWriter out;
        	tree_node tn[];
        	int base_array[];
        	ArrayList<Integer>tree[];
        	int chain_head[];
        	int edge=1;
        	int curr_chain=1;
        	int seg_tree[];
        	
        	
        	
        	public class tree_node{
        		
        		int par;
        		int size;
        		int chain;
        		int pos_segtree;
        		int seg_index;
        		
        	  }
        	
        	@SuppressWarnings("unchecked")
			public void inti(int n)
        	{
        		tree=new ArrayList[n+1];
        		
        		base_array=new int[n+1];
        		
        		tn=new tree_node[n+1];
        		
        		chain_head=new int[n+1];
        		
        		int x=(int) Math.ceil(Math.log(n)/Math.log(2));
        		
        		int max=(int) Math.pow(2,x+1);
        		
        		seg_tree=new int[max];
        	
        	}
        	
        	void dfs(int curr,int pre)
        	{
        		tn[curr]=new tree_node();
        		
        		tn[curr].size=1;
        		
        		tn[curr].par=pre;
        		
        		
        		for(int i=0;i<tree[curr].size();i++)
        		{
        			
        			int ct=tree[curr].get(i);
        			
        			if(ct!=curr&&ct!=tn[curr].par)
        			{
        				dfs(ct,curr);
        				
        				tn[curr].size+=tn[ct].size;
        				
        			}
        			
        		 }
        		
        	 }
        	
        	void hld(int curr_node)
        	{
        		
        		if(chain_head[curr_chain]==0)
        			chain_head[curr_chain]=curr_node;
        		
        		
        		tn[curr_node].chain=curr_chain;
        		
        		tn[curr_node].pos_segtree=edge;
        		
        		base_array[edge++]=curr_node;
        		
        		int spcl_chld=-1;
        		
        		for(int i=0;i<tree[curr_node].size();i++)
        		{
        			int ct=tree[curr_node].get(i);
        			
        			if(ct!=curr_node&&ct!=tn[curr_node].par)
        			{
        				
        				if(spcl_chld==-1||tn[spcl_chld].size<tn[ct].size)
        				{
        					
        					spcl_chld=ct;
        					
        				}
        				
        			}
        			
        		}
        		
        		if(spcl_chld!=-1)
        			hld(spcl_chld);
        				
        		
        		for(int i=0;i<tree[curr_node].size();i++)
        		{
        			int ct=tree[curr_node].get(i);
        			
        			if(ct!=curr_node&&ct!=tn[curr_node].par&&ct!=spcl_chld)
        			{
        				curr_chain++;
        				
        				hld(ct);
        			 
        			}
        			
        		}
        		
        	}
        	
        	
        	void solve() throws IOException
        	{
        		
        		int n=ni();
        		
        		int q=ni();
        		
        		inti(n);
        		
        		for(int i=0;i<n-1;i++)
        		{
        			int u=ni();
        			int v=ni();
        			
        			if(tree[u]==null)
        				tree[u]=new ArrayList<>();
        			if(tree[v]==null)
        				tree[v]=new ArrayList<>();
        			
        			tree[u].add(v);
        			tree[v].add(u);
        			
        		}
        		
        		dfs(1,-1);
        		
        		hld(1);
        		
        		construct_ST(1,n,1);
        		
        	    while(q-->0)
        		{
        			
        			int q1=ni();
        			int q2=ni();
        			
        			if(q1==0)
        			{
        				update(tn[q2].seg_index, q2);
        				
        			}
        			else
        			{
        				int ans=findblack(q2,n);
        				
        				out.println(ans);
        			}
        			
        			
        		}
        		
        		
        		
        	}
        	
        	
        	
        	private void construct_ST(int ss, int se, int i) {
        		
        		
        		if(ss==se)
        		{
        			tn[base_array[ss]].seg_index=i;
        			
        			seg_tree[i]=-1;
        			
        			return;
        		    	
        		}
        		
        		int mid=(ss+se)/2;
        		
        		construct_ST(ss, mid, 2*i);

        		construct_ST(mid+1, se, 2*i+1);
        		
        		seg_tree[i]=-1;
				
			}

			int query(int lo,int hi,int a,int b,int x)
        	{
        		       		
        	     if(hi<a||b<lo)
        	    	 return -1;
        	     if(lo<=a&&b<=hi)
        	    	 return seg_tree[x];
        	     
        	     int mid=(a+b)/2;
        	     
        	     int s=query(lo,hi,a,mid,2*x);
        	     
        	     int r=query(lo,hi,mid+1,b,2*x+1);
        	     
        	     return s!=-1?s:r;
        		
        	}
        	
        	void update(int x,int v)
        	{
        		
        		seg_tree[x]=seg_tree[x]!=-1?-1:v;
        		
        		for( x=(x)/2;x>0;x/=2)
        			seg_tree[x]=seg_tree[2*x]!=-1?seg_tree[2*x]:seg_tree[2*x+1];
        			
        	}
        	
        	int findblack(int x,int n)
        	{
        		int ans=-1;
        		
        		while(true)
        		{
        			
        			int chain_no=tn[x].chain;
        			
        			if(chain_no==1)
        			{
        				int temp=query(1, tn[x].pos_segtree,1,n,1);
        				
        				if(temp!=-1)
        					ans=temp;
        				
        				break;
        			}
        			else
        			{
        				
        				int temp=query(tn[chain_head[chain_no]].pos_segtree,tn[x].pos_segtree,1,n,1);
        				
           				if(temp!=-1) 
        					ans=temp;
        				
        				x=tn[chain_head[chain_no]].par;
        				
        			}
        			
        			}
        		
        		return ans;
        		
        		
        	}
        	
        	void run() throws Exception {
            		is = System.in;
            		out = new PrintWriter(System.out);
            		solve();
            		out.flush();
            	}
             
            	public static void main(String[] args) throws Exception {
            		new Thread(null, new Runnable() {
            			public void run() {
            				try {
            					new HLD().run();
            				} catch (Exception e) {
            					e.printStackTrace();
            				}
            			}
            		}, "1", 1 << 26).start();
        	}
         
        	
        	private byte[] inbuf = new byte[1024];
        	public int lenbuf = 0, ptrbuf = 0;
        	
        	private int readByte() {
        		if (lenbuf == -1)
        			throw new InputMismatchException();
        		if (ptrbuf >= lenbuf) {
        			ptrbuf = 0;
        			try {
        				lenbuf = is.read(inbuf);
        			} catch (IOException e) {
        				throw new InputMismatchException();
        			}
        			if (lenbuf <= 0)
        				return -1;
        		}
        		return inbuf[ptrbuf++];
        	}
         
            private int ni() {
        		int num = 0, b;
        		boolean minus = false;
        		while ((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'))
        			;
        		if (b == '-') {
        			minus = true;
        			b = readByte();
        		}
         
        		while (true) {
        			if (b >= '0' && b <= '9') {
        				num = num * 10 + (b - '0');
        			} else {
        				return minus ? -num : num;
        			}
        			b = readByte();
        		}
        	}
         
        }   