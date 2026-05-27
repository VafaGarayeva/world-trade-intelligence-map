// ==============================================================================
// CENTRAL DATA ENGINE: Core Micro-Simulation Frameworks 
// ==============================================================================
const ciberDataEngine = { 
  TAIWAN: { 
    title: "EVENT: TAIWAN CHIP CRISIS", 
    level: "SEVERE (80% Disruption)", 
    score: "78", 
    width: "80%", 
    worst: "-2.31%", 
    best: "-0.42%", 
    pulsePos: { top: "41%", left: "73.5%" },   
    linesColor: { l1: "#ef4444", l2: "#ef4444", l3: "#22c55e", l4: "#00ffcc", l5: "#f59e0b" }, 
    countries: [ 
      { name: "Germany", impact: "Impact: 82%" }, 
      { name: "Japan", impact: "Impact: 75%" }, 
      { name: "USA", impact: "Impact: 71%" }, 
      { name: "South Korea", impact: "Impact: 68%" }, 
      { name: "Singapore", impact: "Impact: 63%" } 
    ], 
    sectors: [ 
      { name: "Electronics", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Automotive", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Aerospace", status: "HIGH", color: "#f59e0b" }, 
      { name: "Consumer Goods", status: "MEDIUM", color: "#3b82f6" }, 
      { name: "Agriculture", status: "LOW", color: "#22c55e" } 
    ], 
    prices: [ 
      { key: "Semiconductors", change: "+18.3%", cls: "red-text" }, 
      { key: "Crude Oil", change: "+6.7%", cls: "red-text" }, 
      { key: "Lithium", change: "+9.2%", cls: "red-text" }, 
      { key: "Wheat", change: "+2.1%", cls: "red-text" }, 
      { key: "Rare Earth Metals", change: "+12.4%", cls: "red-text" } 
    ], 
    feed: [ 
      "Taiwan semiconductor exports drop 50% overnight.", 
      "Japan electronics sector systemic risk increased.", 
      "Germany automotive production lines at critical risk.", 
      "USA chip shortage predicted to last 3 quarters.", 
      "Global inflation pressure rising via microcomponents." 
    ] 
  }, 
  SUEZ: { 
    title: "EVENT: SUEZ CANAL BLOCK", 
    level: "CRITICAL (90% Blocked)", 
    score: "65", 
    width: "90%", 
    worst: "-1.45%", 
    best: "-0.18%", 
    pulsePos: { top: "40%", left: "47.5%" },   
    linesColor: { l1: "#22c55e", l2: "#ef4444", l3: "#f59e0b", l4: "#00ffcc", l5: "#ef4444" }, 
    countries: [ 
      { name: "Egypt", impact: "Impact: 95%" }, 
      { name: "Netherlands", impact: "Impact: 78%" }, 
      { name: "Italy", impact: "Impact: 74%" }, 
      { name: "UK", impact: "Impact: 61%" }, 
      { name: "France", impact: "Impact: 58%" } 
    ], 
    sectors: [ 
      { name: "Maritime Logistics", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Retail & Apparel", status: "HIGH", color: "#f59e0b" }, 
      { name: "Energy Transport", status: "HIGH", color: "#f59e0b" }, 
      { name: "Consumer Goods", status: "MEDIUM", color: "#3b82f6" }, 
      { name: "Electronics", status: "LOW", color: "#22c55e" } 
    ], 
    prices: [ 
      { key: "Maritime Freight Index", change: "+142.8%", cls: "red-text" }, 
      { key: "Crude Oil", change: "+5.4%", cls: "red-text" }, 
      { key: "Wheat", change: "+8.9%", cls: "red-text" }, 
      { key: "Lithium", change: "+0.8%", cls: "red-text" }, 
      { key: "Rare Earth Metals", change: "+1.5%", cls: "red-text" } 
    ], 
    feed: [ 
      "Container ship gridlock builds up in the Red Sea corridor.", 
      "Rotterdam port reports significant delays in incoming cargo.", 
      "European supply networks face immediate parts shortage." 
    ] 
  }, 
  AZERI: { 
    title: "EVENT: AZERBAIJAN ENERGY SHOCK", 
    level: "HIGH (40% Gas Outage)", 
    score: "52", 
    width: "55%", 
    worst: "-0.85%", 
    best: "-0.05%", 
    pulsePos: { top: "31%", left: "53.5%" },   
    linesColor: { l1: "#00ffcc", l2: "#ef4444", l3: "#22c55e", l4: "#f59e0b", l5: "#3b82f6" }, 
    countries: [ 
      { name: "Italy", impact: "Impact: 64%" }, 
      { name: "Germany", impact: "Impact: 51%" }, 
      { name: "Greece", impact: "Impact: 48%" }, 
      { name: "Turkey", impact: "Impact: 42%" }, 
      { name: "Georgia", impact: "Impact: 35%" } 
    ], 
    sectors: [ 
      { name: "Crude Petroleum", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Natural Gas", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Heavy Industry", status: "HIGH", color: "#f59e0b" }, 
      { name: "Logistics", status: "MEDIUM", color: "#3b82f6" }, 
      { name: "Consumer Goods", status: "LOW", color: "#22c55e" } 
    ], 
    prices: [ 
      { key: "Brent Crude Oil", change: "+14.2%", cls: "red-text" }, 
      { key: "European Natural Gas", change: "+18.9%", cls: "red-text" }, 
      { key: "Electricity Tariffs", change: "+9.5%", cls: "red-text" }, 
      { key: "Semiconductors", change: "0.0%", cls: "muted-text" }, 
      { key: "Wheat", change: "+0.4%", cls: "red-text" } 
    ], 
    feed: [ 
      "Caspian basin export flows decrease due to infrastructure updates.", 
      "European utilities strategic natural gas backups activated.", 
      "South-Eastern European power grids report increased load variance." 
    ] 
  }, 
  CHINA_SHOCK: { 
    title: "EVENT: CHINA TRADE & EXPORT SHOCK", 
    level: "CATASTROPHIC (95% Drop)", 
    score: "91", 
    width: "95%", 
    worst: "-4.82%", 
    best: "-1.10%", 
    pulsePos: { top: "37%", left: "69%" },   
    linesColor: { l1: "#ef4444", l2: "#ef4444", l3: "#ef4444", l4: "#ef4444", l5: "#ef4444" }, 
    countries: [ 
      { name: "USA", impact: "Impact: 89%" }, 
      { name: "Japan", impact: "Impact: 84%" }, 
      { name: "Australia", impact: "Impact: 81%" }, 
      { name: "Germany", impact: "Impact: 76%" }, 
      { name: "South Korea", impact: "Impact: 74%" } 
    ], 
    sectors: [ 
      { name: "Raw Components", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Consumer Electronics", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Automotive", status: "CRITICAL", color: "#ef4444" }, 
      { name: "Consumer Goods", status: "HIGH", color: "#f59e0b" }, 
      { name: "Agriculture", status: "MEDIUM", color: "#3b82f6" } 
    ], 
    prices: [ 
      { key: "Global Manufacturing Index", change: "-22.4%", cls: "green-text" }, 
      { key: "Consumer Price Index", change: "+7.8%", cls: "red-text" }, 
      { key: "Freight Containers", change: "-18.5%", cls: "green-text" }, 
      { key: "Semiconductors", change: "+24.5%", cls: "red-text" }, 
      { key: "Crude Oil", change: "-5.3%", cls: "green-text" } 
    ], 
    feed: [ 
      "Systemic manufacturing freeze reported across major industrial sectors.", 
      "Global retail giants issue warnings on holiday season inventory levels.", 
      "Global trade network structural stability factor drops dramatically." 
    ] 
  } 
}; 

// ==============================================================================
// ORCHESTRATION LAYER: Dynamic State Changes 
// ==============================================================================
function triggerCyberShock(key, element) { 
  const data = ciberDataEngine[key]; 
  if (!data) return;

  // Həm .scenario-list, həm də .scenario-buttons daxilindəki düymələrin aktivliyini sıfırlayırıq
  const btns = document.querySelectorAll('.scen-btn'); 
  btns.forEach(b => b.classList.remove('active')); 
  if (element) element.classList.add('active'); 

  document.getElementById("event-name").innerText = data.title; 
  document.getElementById("shock-lvl").innerText = data.level; 
  document.getElementById("intensity-fill").style.width = data.width; 
  document.getElementById("global-score-num").innerHTML = `${data.score}<span class="small-total">/100</span>`; 
  
  document.getElementById("mc-worst").innerText = data.worst; 
  document.getElementById("mc-best").innerText = data.best; 
  
  const pulse = document.getElementById("center-pulse"); 
  if (pulse) {
    pulse.style.top = data.pulsePos.top; 
    pulse.style.left = data.pulsePos.left; 
  }
  
  if (document.getElementById("flow-line-1")) document.getElementById("flow-line-1").setAttribute("stroke", data.linesColor.l1); 
  if (document.getElementById("flow-line-2")) document.getElementById("flow-line-2").setAttribute("stroke", data.linesColor.l2); 
  if (document.getElementById("flow-line-3")) document.getElementById("flow-line-3").setAttribute("stroke", data.linesColor.l3); 
  if (document.getElementById("flow-line-4")) document.getElementById("flow-line-4").setAttribute("stroke", data.linesColor.l4); 
  if (document.getElementById("flow-line-5")) document.getElementById("flow-line-5").setAttribute("stroke", data.linesColor.l5); 
  
  let countryHtml = ""; 
  data.countries.forEach((c, idx) => { 
    countryHtml += ` 
      <div class="aff-row"> 
        <span>${idx + 1}. ${c.name}</span> 
        <small>${c.impact}</small> 
      </div> 
    `; 
  }); 
  document.getElementById("affected-countries-list").innerHTML = countryHtml; 
   
  let sectorHtml = ""; 
  data.sectors.forEach(s => { 
    sectorHtml += ` 
      <div class="metric-row"> 
        <span>${s.name}</span> 
        <strong style="color: ${s.color}">${s.status}</strong> 
      </div> 
    `; 
  }); 
  document.getElementById("sector-risk-list").innerHTML = sectorHtml; 
   
  let priceHtml = ""; 
  data.prices.forEach(p => { 
    priceHtml += ` 
      <div class="price-row"> 
        <span>${p.key}</span> 
        <strong class="${p.cls}">${p.change}</strong> 
      </div> 
    `; 
  }); 
  document.getElementById("price-impact").innerHTML = priceHtml; 
  
  let feedHtml = ""; 
  const d = new Date(); 
  const timeStr = d.toTimeString().split(' ')[0]; 
  data.feed.forEach(f => { 
    feedHtml += `<div class="feed-item"><span>[${timeStr}]</span> ${f}</div>`; 
  }); 
  document.getElementById("live-feed").innerHTML = feedHtml; 
} 

// ==============================================================================
// EVENTS & LISTENERS INITIALIZATION
// ==============================================================================
document.addEventListener("DOMContentLoaded", function() {
  
  // A. 2D / 3D Görünüş Düymələrinin Klik İdarəsi
  const viewButtons = document.querySelectorAll('.view-toggle button, .map-view-controls button'); 
  viewButtons.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.stopPropagation(); // Klik hadisəsinin xəritə qatlarına keçməsini tamamilə əngəlləyir
      viewButtons.forEach(b => b.classList.remove('active')); 
      btn.classList.add('active'); 
      console.log(`Görünüş dəyişdirildi: ${btn.textContent.trim()}`);
    });
  });

  // B. Ssenari Düymələrinin Klik Eventləri
  const scenarioButtons = document.querySelectorAll('.scen-btn'); 
  scenarioButtons.forEach(button => {
    button.addEventListener('click', () => {
      const text = button.innerText.toUpperCase();
      
      if (text.includes("TAIWAN")) {
        triggerCyberShock('TAIWAN', button);
      } else if (text.includes("SUEZ")) {
        triggerCyberShock('SUEZ', button);
      } else if (text.includes("AZERBAIJAN") || text.includes("AZE")) {
        triggerCyberShock('AZERI', button);
      } else if (text.includes("CHINA")) {
        triggerCyberShock('CHINA_SHOCK', button);
      }
    });
  });

  // C. İlk Açılanda Tayvan Ssenarisini Default Olaraq Yüklə
  const firstBtn = document.querySelector('.scen-btn');
  if (firstBtn) {
    triggerCyberShock('TAIWAN', firstBtn); 
  }
});
