import{d as B,r as d,G as T,a as b,s as D,o as i,g as k,c as a,e as t,u as e,i as m,f as l,H as $,S as w,l as C,J as L,K as M,b as x,M as V,t as h,F as g,_ as I,L as F,k as R,T as y}from"./index-TP_mFFHO.js";const S=B("docbase",()=>{const u=d(""),s=d([]),c=d(null),n=d(null),o=d("");T("/api/docbase/info").get().then(v=>u.value=v.info),T("/api/docbase/docs").get().then(v=>{s.value=v});function p(v){c.value=v,T("/api/docbase/docs/"+btoa(unescape(encodeURIComponent(v.id)))).get().then(A=>{n.value=A})}function f(){}const _=d([{value:"embedding",text:"Embedding"},{value:"keywords",text:"Keywords"}]),r=d("embedding");return{baseInfo:u,docs:s,selectedDocInfo:c,selectDoc:p,docDetail:n,searching:o,searchDoc:f,searchMethod:r,searchMethods:_}}),U={class:"r"},E=b({__name:"SearchingArea",setup(u){const s=d(""),c=S(),{searching:n,searchMethods:o,searchMethod:p}=D(c);return s.value=n.value,(f,_)=>(i(),k("div",U,[a(e(g),{vertical:"",gap:"large"},{default:t(()=>[a(e(m),{level:3},{default:t(()=>[l("Searching - ")]),_:1}),a(e($),{value:s.value,"onUpdate:value":_[0]||(_[0]=r=>s.value=r)},null,8,["value"]),a(e(w),{value:e(p),"onUpdate:value":_[1]||(_[1]=r=>C(p)?p.value=r:null)},{default:t(()=>[(i(!0),k(L,null,M(e(o),r=>(i(),x(e(V),{value:r.value},{default:t(()=>[l(h(r.text),1)]),_:2},1032,["value"]))),256))]),_:1},8,["value"])]),_:1})]))}}),K=I(E,[["__scopeId","data-v-fb78d189"]]),N=b({__name:"DocListArea",setup(u){const s=S(),{docs:c}=D(s);function n(o){s.selectDoc(o)}return(o,p)=>(i(),x(e(g),{gap:"middle",vertical:""},{default:t(()=>[a(e(m),{level:3},{default:t(()=>[l("Docs")]),_:1}),a(e(R),{size:"large",bordered:"",dataSource:e(c)},{renderItem:t(({item:f})=>[a(e(F),{onClick:_=>n(f),item:f},{default:t(()=>[l(h(f.id),1)]),_:2},1032,["onClick","item"])]),_:1},8,["dataSource"])]),_:1}))}}),z=b({__name:"DocDetailArea",setup(u){const{docDetail:s}=D(S());return(c,n)=>(i(),x(e(g),{vertical:"",gap:"middle",class:"r"},{default:t(()=>[a(e(m),{level:3},{default:t(()=>[l("Doc Detail - ")]),_:1}),a(e(m),{level:5},{default:t(()=>{var o;return[l(h(((o=e(s))==null?void 0:o.name)??""),1)]}),_:1}),a(e(y),null,{default:t(()=>{var o;return[l(h(((o=e(s))==null?void 0:o.content)??""),1)]}),_:1})]),_:1}))}}),G=I(z,[["__scopeId","data-v-ca6ce804"]]),H=b({__name:"BaseInfoArea",setup(u){const s=S(),{baseInfo:c}=D(s);return(n,o)=>(i(),x(e(g),{vertical:"",gap:"middle",class:"baseInfo"},{default:t(()=>[a(e(m),{level:3},{default:t(()=>[l("DocBase Info -")]),_:1}),a(e(y),null,{default:t(()=>[l(h(e(c)),1)]),_:1})]),_:1}))}}),J=I(H,[["__scopeId","data-v-63b1169c"]]),O={class:"r"},j=b({__name:"DocbaseView",setup(u){return(s,c)=>{const n=K;return i(),k("div",O,[a(e(g),{gap:"large"},{default:t(()=>[a(J),a(N),a(e(g),{vertical:"",gap:"large"},{default:t(()=>[a(n),a(G)]),_:1})]),_:1})])}}}),P=I(j,[["__scopeId","data-v-abde25cd"]]);export{P as default};
