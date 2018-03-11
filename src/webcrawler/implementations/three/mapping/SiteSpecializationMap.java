package webcrawler.implementations.three.mapping;

import webcrawler.common.SiteSpecialization;

import java.util.ArrayList;

public class SiteSpecializationMap
{
    public ArrayList<SiteSpecialization> predefined = new ArrayList<>();

    //

    public SiteSpecializationMap()
    {
        this.predefined.add(new SiteSpecialization("https://google.com", 2, 2, false));

        //

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Computer_science", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Mathematics", 2, 2, false));

        /*

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Logic", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Constitution", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Church", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Xenu", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Religion", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Spirituality", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Philosophy", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Network_analysis_(electrical_circuits)", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Calculus", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Trigonometry", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Psychology", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Psychiatry", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Pharmacology", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Algebra", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Linear_algebra", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Science", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Chemistry", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Intelligence_quotient", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/President_of_the_United_States", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Vice_President_of_the_United_States", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_by_population", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Wikipedia", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Pope", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Pope_Francis", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/L._Ron_Hubbard", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Truth", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Java_(programming_language)", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/C_(programming_language)", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/C%2B%2B", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://en.wikipedia.org/wiki/Music", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?source=hp&q=computer+software&oq=computer+software", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?source=hp&q=system+models&oq=computer+software", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?source=hp&q=reality&oq=reality", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?source=hp&q=teaching&oq=teaching", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?source=hp&q=organization&oq=organization", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?source=hp&q=philosophy&oq=philosophy", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?source=hp&q=honor&oq=honor", 2, 2, false));

        //

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=church&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=life&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=java+programming&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=c+programming&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=religion&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=scientology&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=president%20trump&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=united%20states%20constitution&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=presidential%20signing%20orders&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=state%20of%20the%20union&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=north%20carolina%20law&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=federalism&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=germany&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=latin%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=italian%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=spanish%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=portugese%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=hindi%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=bengali%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=javanese%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=punjabi%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=landha%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=arabic%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=german%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=french%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=afrikaans%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=dutch%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=chinese%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=mandarin%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=cantonese%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=japanese%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=swahili%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=russian%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=icelandic%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=polynesian%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=inuit%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=korean%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=tagalog%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=tsimshian%20dictionary&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=shakespeare&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=existentialism&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=engineering&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=calculus&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=calculus&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=linear%20algebra&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=trigonometry%20algebra&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=trigonometry&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=fourier&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=chess&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=crime&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=children&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=psychology&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=psychiatry&tbs=,bkv:f&num=100", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://www.google.com/search?tbo=p&tbm=bks&q=chemistry&tbs=,bkv:f&num=100", 2, 2, false));

        //

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/?ned=us&gl=US&hl=en", 2, 2, false));                   //usa

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/?ned=uk&gl=GB&hl=en-GB", 2, 2, false));                //uk

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=ar_me&hl=ar&gl=ME", 2, 2, false));       //arab

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=en_zw&hl=en&gl=ZW", 2, 2, false));       //zimbabwe

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=es_co&hl=es-219&gl=CO", 2, 2, false));   //columbia

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=en_bw&hl=en&gl=BW", 2, 2, false));       //botswana

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=cn&hl=zh-CN&gl=CN", 2, 2, false));       //china

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=it&hl=it&gl=IT", 2, 2, false));          //italy

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=hk&hl=zh-HK&gl=HK", 2, 2, false));       //hong kong

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=nl_nl&hl=nl&gl=NL", 2, 2, false));       //netherlands

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=en_il&hl=en&gl=IL", 2, 2, false));       //israel

        this.predefined.add(new SiteSpecialization("https://news.google.com/news/headlines?ned=pt-PT_pt&hl=pt-PT&gl=PT", 2, 2, false)); //portugal

        //

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=trucks&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=cars&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=bmw&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=bitcoin&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=mercedes&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=trailers&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=porsche&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=computer+hardware&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=computer+software&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=ham+radio&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=telescope&sort=rel", 2, 2, false));

        this.predefined.add(new SiteSpecialization("https://raleigh.craigslist.org/search/sss?query=solar+panels&sort=rel", 2, 2, false));

        //

        this.predefined.add(new SiteSpecialization("https://www.yelp.com/search?find_desc=Bars&find_loc=North+Carolina",2,2, true));

        this.predefined.add(new SiteSpecialization("https://www.yelp.com/search?find_desc=Bars&find_loc=Alabama",2,2, true));

        this.predefined.add(new SiteSpecialization("https://www.yelp.com/search?find_desc=Bars&find_loc=Fayetteville",2,2, true));

        this.predefined.add(new SiteSpecialization("https://www.yelp.com/search?find_desc=Bars&find_loc=Raleigh",2,2, true));

        this.predefined.add(new SiteSpecialization("https://www.yelp.com/search?find_desc=Bars&find_loc=Raleigh",2,2, true));

        */
    }
}