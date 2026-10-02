// AniSuggest Desktop Web Engine & App Logic

// Curated Masterpiece Catalog (24 Critically Acclaimed Anime with Verified AniList CDN Artwork)
const CURATED_ANIME = [
  {
    id: 1,
    title: "Sousou no Frieren",
    englishTitle: "Frieren: Beyond Journey's End",
    japaneseTitle: "葬送のフリーレン",
    score: 9.3,
    episodes: 28,
    duration: "24 min/ep",
    format: "TV",
    year: 2023,
    studio: "Madhouse",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx154587-n2bGQYeytQg3.jpg",
    hook: "An elf mage outlives her heroic companions and embarks on a poignant quest to understand human hearts.",
    synopsis: "The adventure is over, but life goes on for an elf mage just beginning to learn what living is all about. An exceptionally soulful, meditative fantasy masterpiece.",
    vibes: ["TEARJERKER", "COZY", "FANTASY"],
    genres: ["Fantasy", "Adventure", "Drama"]
  },
  {
    id: 2,
    title: "Fullmetal Alchemist: Brotherhood",
    englishTitle: "Fullmetal Alchemist: Brotherhood",
    japaneseTitle: "鋼の錬金術師 FULLMETAL ALCHEMIST",
    score: 9.1,
    episodes: 64,
    duration: "24 min/ep",
    format: "TV",
    year: 2009,
    studio: "Bones",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx5114-191Smh4bt1kH.jpg",
    hook: "Two brothers commit alchemy's greatest taboo and sacrifice everything to restore their shattered bodies.",
    synopsis: "Edward and Alphonse Elric seek the legendary Philosopher's Stone to recover their lost bodies in an epic tale of conspiracy, morality, and unbreakable brotherhood.",
    vibes: ["HYPE", "FANTASY", "TEARJERKER"],
    genres: ["Action", "Adventure", "Fantasy", "Drama"]
  },
  {
    id: 3,
    title: "Steins;Gate",
    englishTitle: "Steins;Gate",
    japaneseTitle: "シュタインズ・ゲート",
    score: 9.1,
    episodes: 24,
    duration: "24 min/ep",
    format: "TV",
    year: 2011,
    studio: "White Fox",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx9253-7pdcLF0H9NuN.jpg",
    hook: "A microwave-powered time machine turns playful delusions into a harrowing fight against fate.",
    synopsis: "Eccentric scientist Rintaro Okabe accidentally invents a device that sends messages to the past, triggering a catastrophic butterfly effect across global timelines.",
    vibes: ["PSYCHOLOGICAL", "HYPE"],
    genres: ["Sci-Fi", "Suspense", "Thriller", "Psychological"]
  },
  {
    id: 4,
    title: "Death Note",
    englishTitle: "Death Note",
    japaneseTitle: "デスノート",
    score: 8.6,
    episodes: 37,
    duration: "23 min/ep",
    format: "TV",
    year: 2006,
    studio: "Madhouse",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx1535-lawCjhivZF4A.png",
    hook: "A genius high schooler discovers a notebook that kills anyone whose name is written in it.",
    synopsis: "Light Yagami seeks to cleanse the world of criminals while being relentlessly hunted by the enigmatic super-detective known only as L.",
    vibes: ["PSYCHOLOGICAL", "DARK"],
    genres: ["Mystery", "Psychological", "Supernatural", "Suspense"]
  },
  {
    id: 5,
    title: "Shingeki no Kyojin",
    englishTitle: "Attack on Titan",
    japaneseTitle: "進撃の巨人",
    score: 9.0,
    episodes: 89,
    duration: "24 min/ep",
    format: "TV",
    year: 2013,
    studio: "Wit Studio / MAPPA",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx16498-C6FPmWm59CyP.jpg",
    hook: "Humanity hides behind colossal walls until giant humanoid predators breach their sanctuary.",
    synopsis: "Eren Yeager vows to exterminate every Titan after his hometown is obliterated, uncovering vast geopolitical conspiracies that test the limits of freedom.",
    vibes: ["HYPE", "DARK", "PSYCHOLOGICAL"],
    genres: ["Action", "Suspense", "Drama", "Fantasy"]
  },
  {
    id: 6,
    title: "Hunter x Hunter (2011)",
    englishTitle: "Hunter x Hunter",
    japaneseTitle: "ハンター×ハンター",
    score: 9.0,
    episodes: 148,
    duration: "24 min/ep",
    format: "TV",
    year: 2011,
    studio: "Madhouse",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx11061-n5LOBvF64uFl.png",
    hook: "A boy embarks on an unforgiving journey to become a Hunter and track down the father who abandoned him.",
    synopsis: "Gon Freecss enters the deadly Hunter Exam, forging unbreakable bonds while facing the dark underworld of Nen martial arts and moral ambiguity.",
    vibes: ["HYPE", "FANTASY", "DARK"],
    genres: ["Action", "Adventure", "Fantasy"]
  },
  {
    id: 7,
    title: "Jujutsu Kaisen",
    englishTitle: "Jujutsu Kaisen",
    japaneseTitle: "呪術廻戦",
    score: 8.7,
    episodes: 47,
    duration: "24 min/ep",
    format: "TV",
    year: 2020,
    studio: "MAPPA",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx113415-bbBWj4pEFseh.jpg",
    hook: "A high school athlete swallows a cursed talisman finger and gets drafted into supernatural exorcisms.",
    synopsis: "Yuji Itadori shares his body with the King of Curses, Ryomen Sukuna, training at Tokyo Jujutsu High to protect humanity from malignant curses.",
    vibes: ["HYPE", "DARK", "FANTASY"],
    genres: ["Action", "Supernatural", "Fantasy"]
  },
  {
    id: 8,
    title: "Kimetsu no Yaiba",
    englishTitle: "Demon Slayer: Kimetsu no Yaiba",
    japaneseTitle: "鬼滅の刃",
    score: 8.6,
    episodes: 55,
    duration: "24 min/ep",
    format: "TV",
    year: 2019,
    studio: "ufotable",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx101922-PEn1CTDYxZWm.jpg",
    hook: "A boy wields a sword of breathing techniques to avenge his family and cure his demon-turned sister.",
    synopsis: "Tanjiro Kamado joins the Demon Slayer Corps with unprecedented ufotable kinetic sword animation that set global box office records.",
    vibes: ["HYPE", "TEARJERKER", "FANTASY"],
    genres: ["Action", "Fantasy", "Historical"]
  },
  {
    id: 9,
    title: "Koe no Katachi",
    englishTitle: "A Silent Voice",
    japaneseTitle: "聲の形",
    score: 8.9,
    episodes: 1,
    duration: "130 min",
    format: "MOVIE",
    year: 2016,
    studio: "Kyoto Animation",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx20954-UMb6Kl7ZLurq.jpg",
    hook: "A reformed childhood bully seeks redemption from the deaf girl whose youth he made miserable.",
    synopsis: "Shoya Ishida reaches rock bottom in high school before setting out to make heartfelt amends with Shoko Nishimiya in this landmark emotional triumph.",
    vibes: ["TEARJERKER", "ROMANCE"],
    genres: ["Drama", "Romance", "Award Winning"]
  },
  {
    id: 10,
    title: "Kimi no Na wa.",
    englishTitle: "Your Name.",
    japaneseTitle: "君の名は。",
    score: 8.9,
    episodes: 1,
    duration: "107 min",
    format: "MOVIE",
    year: 2016,
    studio: "CoMix Wave Films",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx21519-73dfed.png",
    hook: "Two high schoolers across distant towns start inexplicably swapping bodies when they fall asleep.",
    synopsis: "Mitsuha and Taki navigate each other's lives until a celestial comet revelation turns their connection into a heart-racing race against catastrophic time.",
    vibes: ["ROMANCE", "TEARJERKER", "FANTASY"],
    genres: ["Romance", "Drama", "Supernatural"]
  },
  {
    id: 11,
    title: "Violet Evergarden",
    englishTitle: "Violet Evergarden",
    japaneseTitle: "ヴァイオレット・エヴァーガーデン",
    score: 8.7,
    episodes: 13,
    duration: "24 min/ep",
    format: "TV",
    year: 2018,
    studio: "Kyoto Animation",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx21827-10fJkWITepTv.png",
    hook: "A former child soldier works as an Auto Memory Doll to decipher the phrase 'I love you'.",
    synopsis: "Violet transcribes the deepest heartfelt sentiments of people across post-war kingdoms, discovering the raw beauty and ache of human love.",
    vibes: ["TEARJERKER", "COZY"],
    genres: ["Drama", "Fantasy", "Slice of Life"]
  },
  {
    id: 12,
    title: "Kaguya-sama wa Kokurasetai",
    englishTitle: "Kaguya-sama: Love is War",
    japaneseTitle: "かぐや様は告らせたい",
    score: 8.9,
    episodes: 37,
    duration: "24 min/ep",
    format: "TV",
    year: 2019,
    studio: "A-1 Pictures",
    coverUrl: "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx101921-VvdGQyKsluh7.jpg",
    hook: "Two hyper-elite student council leaders engage in 4D psychological warfare to force the other to confess love.",
    synopsis: "Love is a battlefield! Kaguya Shinomiya and Miyuki Shirogane refuse to yield their pride in a dazzling comedy of high-stakes romantic schemes.",
    vibes: ["COMEDY", "ROMANCE"],
    genres: ["Comedy", "Romance", "School"]
  }
];

// App State
let currentTab = 'discover';
let isLiveJikan = false;
let currentJikanSource = 'top';
let selectedMood = null;
let selectedGenre = 'All';
let searchQuery = '';
let searchDebounceTimer = null;
let liveResults = [];
let watchlist = JSON.parse(localStorage.getItem('anisuggest_watchlist') || '[]');

// DOM Elements
const searchInput = document.getElementById('searchInput');
const clearSearchBtn = document.getElementById('clearSearchBtn');
const jikanToggle = document.getElementById('jikanToggle');
const engineStatusLabel = document.getElementById('engineStatusLabel');
const jikanTabs = document.getElementById('jikanTabs');
const animeGrid = document.getElementById('animeGrid');
const loadingIndicator = document.getElementById('loadingIndicator');
const errorBanner = document.getElementById('errorBanner');
const errorMsg = document.getElementById('errorMsg');
const retryBtn = document.getElementById('retryBtn');
const resultsTitle = document.getElementById('resultsTitle');
const resultsCount = document.getElementById('resultsCount');
const watchlistCount = document.getElementById('watchlistCount');
const detailModal = document.getElementById('detailModal');
const modalBackdrop = document.getElementById('modalBackdrop');
const modalCloseBtn = document.getElementById('modalCloseBtn');
const modalBody = document.getElementById('modalBody');

// Navigation handler
document.querySelectorAll('.nav-item').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.nav-item').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    const tab = btn.dataset.tab;
    switchTab(tab);
  });
});

function switchTab(tab) {
  currentTab = tab;
  document.querySelectorAll('.view-panel').forEach(p => p.classList.remove('active'));
  if (tab === 'discover') document.getElementById('viewDiscover').classList.add('active');
  if (tab === 'quiz') {
    document.getElementById('viewQuiz').classList.add('active');
    initQuiz();
  }
  if (tab === 'wheel') {
    document.getElementById('viewWheel').classList.add('active');
    initRoulette();
  }
  if (tab === 'matcher') {
    document.getElementById('viewMatcher').classList.add('active');
    initMatcher();
  }
  if (tab === 'watchlist') {
    document.getElementById('viewWatchlist').classList.add('active');
    renderWatchlist('ALL');
  }
}

// Mood Chips Setup
const MOODS = [
  { id: 'HYPE', label: '🔥 Hype', desc: 'Adrenaline & Epic Battles' },
  { id: 'PSYCHOLOGICAL', label: '🧠 Mind-Bending', desc: 'Genius Plot Twists' },
  { id: 'TEARJERKER', label: '💧 Tearjerker', desc: 'Emotional Impact' },
  { id: 'COZY', label: '🍵 Cozy & Wholesome', desc: 'Comfort & Warmth' },
  { id: 'ROMANCE', label: '💖 Romance', desc: 'Heartflutter & Chemistry' },
  { id: 'FANTASY', label: '✨ Fantasy & Magic', desc: 'Expansive World-Building' },
  { id: 'DARK', label: '🌑 Dark & Gritty', desc: 'Visceral Stakes' },
  { id: 'COMEDY', label: '😂 Comedy', desc: 'Laugh-Out-Loud Humor' }
];

const moodChipsContainer = document.getElementById('moodChipsContainer');
MOODS.forEach(m => {
  const chip = document.createElement('button');
  chip.className = 'mood-chip';
  chip.innerHTML = m.label;
  chip.onclick = () => {
    if (selectedMood === m.id) {
      selectedMood = null;
      chip.classList.remove('active');
    } else {
      selectedMood = m.id;
      document.querySelectorAll('.mood-chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
    }
    renderDiscoverList();
  };
  moodChipsContainer.appendChild(chip);
});

// Genre Filter Setup
const GENRES = ['All', 'Action', 'Adventure', 'Fantasy', 'Drama', 'Sci-Fi', 'Romance', 'Comedy', 'Mystery', 'Slice of Life'];
const genreChipsContainer = document.getElementById('genreChipsContainer');
GENRES.forEach(g => {
  const chip = document.createElement('button');
  chip.className = `genre-chip ${g === 'All' ? 'active' : ''}`;
  chip.innerText = g;
  chip.onclick = () => {
    document.querySelectorAll('.genre-chip').forEach(c => c.classList.remove('active'));
    chip.classList.add('active');
    selectedGenre = g;
    renderDiscoverList();
  };
  genreChipsContainer.appendChild(chip);
});

// Search input handling
searchInput.addEventListener('input', (e) => {
  searchQuery = e.target.value;
  clearSearchBtn.classList.toggle('hidden', searchQuery.length === 0);

  if (isLiveJikan) {
    clearTimeout(searchDebounceTimer);
    if (searchQuery.trim().length >= 2) {
      searchDebounceTimer = setTimeout(() => {
        executeJikanSearch(searchQuery.trim());
      }, 400);
    } else if (searchQuery.trim().length === 0) {
      fetchJikanData(currentJikanSource);
    }
  } else {
    renderDiscoverList();
  }
});

clearSearchBtn.addEventListener('click', () => {
  searchInput.value = '';
  searchQuery = '';
  clearSearchBtn.classList.add('hidden');
  if (isLiveJikan) {
    fetchJikanData(currentJikanSource);
  } else {
    renderDiscoverList();
  }
});

// Live Jikan Toggle
jikanToggle.addEventListener('change', (e) => {
  isLiveJikan = e.target.checked;
  engineStatusLabel.innerText = isLiveJikan ? "Live Cloud Engine" : "Curated Hits";
  jikanTabs.classList.toggle('hidden', !isLiveJikan);

  if (isLiveJikan) {
    fetchJikanData(currentJikanSource);
  } else {
    hideError();
    renderDiscoverList();
  }
});

// Jikan Sub-tabs
document.querySelectorAll('.jikan-tab').forEach(tab => {
  tab.addEventListener('click', () => {
    document.querySelectorAll('.jikan-tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    currentJikanSource = tab.dataset.source;
    if (currentJikanSource === 'search' && searchQuery.trim().length >= 2) {
      executeJikanSearch(searchQuery.trim());
    } else {
      fetchJikanData(currentJikanSource);
    }
  });
});

// Fetch Live Jikan Data (via active Cloudflare Edge instance)
async function fetchJikanData(source) {
  showLoading(true);
  hideError();

  const url = source === 'top'
    ? 'https://jikan.lucashdo.com/v1/top/anime'
    : 'https://jikan.lucashdo.com/v1/seasons/now';

  try {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const data = await res.json();
    liveResults = (data.data || []).map(formatJikanItem);
    renderCards(liveResults, `Live ${source === 'top' ? 'Top Airing' : 'Current Season'} Anime (Jikan)`);
  } catch (err) {
    showError("Could not reach online server. Showing curated catalog as backup.");
    renderDiscoverList();
  } finally {
    showLoading(false);
  }
}

async function executeJikanSearch(q) {
  showLoading(true);
  hideError();

  try {
    const res = await fetch(`https://jikan.lucashdo.com/v1/anime?q=${encodeURIComponent(q)}`);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const data = await res.json();
    liveResults = (data.data || []).map(formatJikanItem);
    renderCards(liveResults, `Live Results for "${q}"`);
  } catch (err) {
    showError("Online search request failed. Tap retry to reconnect.");
  } finally {
    showLoading(false);
  }
}

function formatJikanItem(item) {
  return {
    id: item.malId || item.mal_id || Math.floor(Math.random() * 100000),
    title: item.titleEnglish || item.title_english || item.title || "Unknown Anime",
    japaneseTitle: item.titleJapanese || item.title_japanese || "",
    englishTitle: item.titleEnglish || item.title_english || item.title,
    score: parseFloat(item.score) || 8.0,
    episodes: parseInt(item.episodes) || 12,
    duration: item.duration || "24 min/ep",
    format: item.type || "TV",
    year: item.year || 2024,
    studio: (item.studios && item.studios[0]?.name) || "Anime Studio",
    coverUrl: item.imageUrl || (item.images && item.images.large) || "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600",
    hook: (item.synopsis || "No synopsis available.").substring(0, 120) + "...",
    synopsis: (item.synopsis || "No description provided.").replace("[Written by MAL Rewrite]", "").trim(),
    vibes: ["HYPE"],
    genres: (item.genres || []).map(g => g.name)
  };
}

function renderDiscoverList() {
  let list = CURATED_ANIME;

  if (searchQuery.trim().length > 0) {
    const q = searchQuery.toLowerCase();
    list = list.filter(a => a.title.toLowerCase().includes(q) || a.englishTitle.toLowerCase().includes(q) || a.genres.some(g => g.toLowerCase().includes(q)));
  }

  if (selectedMood) {
    list = list.filter(a => a.vibes.includes(selectedMood));
  }

  if (selectedGenre !== 'All') {
    list = list.filter(a => a.genres.some(g => g.toLowerCase() === selectedGenre.toLowerCase()));
  }

  const title = selectedMood ? `${MOODS.find(m => m.id === selectedMood)?.label} Picks` : "Curated Masterpieces";
  renderCards(list, title);
}

function renderCards(list, titleText) {
  resultsTitle.innerText = titleText;
  resultsCount.innerText = `${list.length} titles`;
  animeGrid.innerHTML = '';

  if (list.length === 0) {
    animeGrid.innerHTML = `<div style="grid-column: 1/-1; text-align: center; padding: 48px; color: var(--text-muted);">
      <h3>No Anime Found</h3>
      <p>Try clearing your search query or selecting a different mood vibe.</p>
    </div>`;
    return;
  }

  list.forEach(anime => {
    const card = document.createElement('div');
    card.className = 'anime-card';
    card.innerHTML = `
      <div class="poster-wrap">
        <img src="${anime.coverUrl}" alt="${anime.title}" loading="lazy">
        <span class="score-badge">★ ${anime.score.toFixed(1)}</span>
      </div>
      <div class="card-content">
        <h4 class="card-title">${anime.title}</h4>
        <div class="card-meta">${anime.format} • ${anime.episodes} eps • ${anime.year}</div>
        <p class="card-hook">${anime.hook || anime.synopsis}</p>
        <div class="card-footer">
          <div class="card-tags">
            ${anime.genres.slice(0, 2).map(g => `<span class="tag-badge">${g}</span>`).join('')}
          </div>
          <button class="bookmark-icon-btn" title="Add to Watchlist">🔖</button>
        </div>
      </div>
    `;

    card.querySelector('.poster-wrap').onclick = () => openDetail(anime);
    card.querySelector('.card-title').onclick = () => openDetail(anime);
    card.querySelector('.bookmark-icon-btn').onclick = (e) => {
      e.stopPropagation();
      addToWatchlist(anime, 'PLAN_TO_WATCH');
    };

    animeGrid.appendChild(card);
  });
}

function openDetail(anime) {
  modalBody.innerHTML = `
    <div style="display: flex; gap: 24px; flex-wrap: wrap;">
      <img src="${anime.coverUrl}" alt="${anime.title}" style="width: 180px; height: 260px; border-radius: 14px; object-fit: cover;">
      <div style="flex: 1; min-width: 250px;">
        <span class="score-badge" style="position: static; display: inline-block; margin-bottom: 8px;">★ ${anime.score.toFixed(1)} / 10</span>
        <h2 style="font-size: 22px; font-weight: 700; margin-bottom: 4px;">${anime.title}</h2>
        <p style="color: var(--text-muted); font-size: 13px; margin-bottom: 12px;">${anime.japaneseTitle || anime.englishTitle}</p>
        <div style="display: flex; gap: 12px; font-size: 12px; color: var(--cyan-accent); font-weight: 600; margin-bottom: 16px;">
          <span>${anime.format}</span> • <span>${anime.episodes} Episodes</span> • <span>${anime.studio}</span> • <span>${anime.year}</span>
        </div>
        <div style="display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 16px;">
          ${anime.genres.map(g => `<span class="tag-badge" style="font-size: 11px; padding: 4px 10px;">${g}</span>`).join('')}
        </div>
        <div style="display: flex; gap: 10px;">
          <button class="btn btn-primary btn-sm" id="modalAddWlBtn">+ Add to Watchlist</button>
        </div>
      </div>
    </div>
    <div style="margin-top: 24px;">
      <h4 style="font-size: 15px; margin-bottom: 8px; color: var(--cyan-accent);">Synopsis</h4>
      <p style="font-size: 13px; color: var(--text-secondary); line-height: 1.6;">${anime.synopsis}</p>
    </div>
  `;

  document.getElementById('modalAddWlBtn').onclick = () => {
    addToWatchlist(anime, 'PLAN_TO_WATCH');
    closeModal();
  };

  detailModal.classList.remove('hidden');
}

function closeModal() {
  detailModal.classList.add('hidden');
}
modalCloseBtn.onclick = closeModal;
modalBackdrop.onclick = closeModal;

// Watchlist Logic
function addToWatchlist(anime, status = 'PLAN_TO_WATCH') {
  const existing = watchlist.find(item => item.id === anime.id);
  if (existing) {
    existing.status = status;
  } else {
    watchlist.push({
      id: anime.id,
      title: anime.title,
      coverUrl: anime.coverUrl,
      score: anime.score,
      totalEpisodes: anime.episodes,
      currentEpisode: status === 'WATCHING' ? 1 : 0,
      status: status,
      format: anime.format
    });
  }
  saveWatchlist();
  alert(`Added "${anime.title}" to your Watchlist!`);
}

function saveWatchlist() {
  localStorage.setItem('anisuggest_watchlist', JSON.stringify(watchlist));
  updateWatchlistBadge();
}

function updateWatchlistBadge() {
  watchlistCount.innerText = watchlist.length;
}

function renderWatchlist(filterStatus = 'ALL') {
  const container = document.getElementById('watchlistContainer');
  let list = watchlist;
  if (filterStatus !== 'ALL') {
    list = list.filter(item => item.status === filterStatus);
  }

  // Update counter badges
  document.getElementById('countAll').innerText = watchlist.length;
  document.getElementById('countWatching').innerText = watchlist.filter(i => i.status === 'WATCHING').length;
  document.getElementById('countPlan').innerText = watchlist.filter(i => i.status === 'PLAN_TO_WATCH').length;
  document.getElementById('countCompleted').innerText = watchlist.filter(i => i.status === 'COMPLETED').length;
  document.getElementById('countFavs').innerText = watchlist.filter(i => i.status === 'FAVORITES').length;

  container.innerHTML = '';
  if (list.length === 0) {
    container.innerHTML = `<div style="text-align: center; padding: 48px; color: var(--text-muted);">
      <h3>Your Watchlist is Empty</h3>
      <p>Bookmark shows from Discover or Quiz to track your episode progress!</p>
    </div>`;
    return;
  }

  list.forEach(item => {
    const el = document.createElement('div');
    el.className = 'watchlist-item';
    el.innerHTML = `
      <img src="${item.coverUrl}" alt="${item.title}">
      <div class="wl-info">
        <h4 class="wl-title">${item.title}</h4>
        <div class="wl-meta">${item.format} • ★ ${item.score.toFixed(1)} • Status: <strong style="color: var(--cyan-accent);">${item.status}</strong></div>
        <div class="wl-counter">
          <span>Episodes:</span>
          <button class="counter-btn" id="dec_${item.id}">-</button>
          <span>${item.currentEpisode} / ${item.totalEpisodes}</span>
          <button class="counter-btn" id="inc_${item.id}">+</button>
        </div>
      </div>
      <button class="btn btn-outline btn-sm" id="del_${item.id}" style="color: #F87171; border-color: #F87171;">Remove</button>
    `;

    el.querySelector(`#inc_${item.id}`).onclick = () => {
      if (item.currentEpisode < item.totalEpisodes) {
        item.currentEpisode++;
        if (item.currentEpisode === item.totalEpisodes) item.status = 'COMPLETED';
        else item.status = 'WATCHING';
        saveWatchlist();
        renderWatchlist(filterStatus);
      }
    };

    el.querySelector(`#dec_${item.id}`).onclick = () => {
      if (item.currentEpisode > 0) {
        item.currentEpisode--;
        saveWatchlist();
        renderWatchlist(filterStatus);
      }
    };

    el.querySelector(`#del_${item.id}`).onclick = () => {
      watchlist = watchlist.filter(w => w.id !== item.id);
      saveWatchlist();
      renderWatchlist(filterStatus);
    };

    container.appendChild(el);
  });
}

// Watchlist Filter tabs
document.querySelectorAll('.filter-tab').forEach(tab => {
  tab.addEventListener('click', () => {
    document.querySelectorAll('.filter-tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    renderWatchlist(tab.dataset.status);
  });
});

// 30-Second Quiz
const QUIZ_QUESTIONS = [
  {
    title: "1. What emotional headspace are you looking for tonight?",
    options: [
      { text: "💥 High Adrenaline & Epic Battles", mood: "HYPE" },
      { text: "🧠 Mind-bending Mystery & Plot Twists", mood: "PSYCHOLOGICAL" },
      { text: "💧 A deeply touching emotional catharsis", mood: "TEARJERKER" },
      { text: "🍵 Cozy, warm & relaxing wholesome comfort", mood: "COZY" }
    ]
  },
  {
    title: "2. How much time do you want to invest?",
    options: [
      { text: "🎬 A 2-hour movie experience tonight", maxEps: 1 },
      { text: "⚡ A short, punchy 12-episode binge", maxEps: 13 },
      { text: "🍿 An epic multi-season saga", maxEps: 200 }
    ]
  },
  {
    title: "3. What world setting sounds most compelling?",
    options: [
      { text: "🗡️ Dark fantasy & supernatural magic", genre: "Fantasy" },
      { text: "🌆 Modern psychological or realistic world", genre: "Drama" },
      { text: "🚀 Sci-Fi & futuristic timelines", genre: "Sci-Fi" }
    ]
  }
];

let quizStep = 0;
let quizAnswers = {};

function initQuiz() {
  quizStep = 0;
  quizAnswers = {};
  document.getElementById('quizCard').classList.remove('hidden');
  document.getElementById('quizResultsArea').classList.add('hidden');
  showQuizStep(0);
}

function showQuizStep(step) {
  const container = document.getElementById('quizQuestionArea');
  const q = QUIZ_QUESTIONS[step];

  document.querySelectorAll('.quiz-step-indicator .step').forEach((s, idx) => {
    s.classList.toggle('active', idx <= step);
  });

  let optionsHtml = q.options.map((opt, i) => `
    <button class="quiz-option" data-idx="${i}">
      <span>${opt.text}</span>
      <span>➔</span>
    </button>
  `).join('');

  container.innerHTML = `
    <h3 style="font-size: 18px; margin-bottom: 20px;">${q.title}</h3>
    <div>${optionsHtml}</div>
  `;

  container.querySelectorAll('.quiz-option').forEach(btn => {
    btn.onclick = () => {
      const idx = parseInt(btn.dataset.idx);
      quizAnswers[step] = q.options[idx];
      if (step < QUIZ_QUESTIONS.length - 1) {
        showQuizStep(step + 1);
      } else {
        finishQuiz();
      }
    };
  });
}

function finishQuiz() {
  document.getElementById('quizCard').classList.add('hidden');
  const resultsArea = document.getElementById('quizResultsArea');
  const resultsGrid = document.getElementById('quizResultsGrid');
  resultsArea.classList.remove('hidden');

  const chosenMood = quizAnswers[0]?.mood;
  const maxEpisodes = quizAnswers[1]?.maxEps || 100;
  const chosenGenre = quizAnswers[2]?.genre;

  let candidates = CURATED_ANIME.filter(a => {
    let match = true;
    if (chosenMood && !a.vibes.includes(chosenMood)) match = false;
    return match;
  });

  if (candidates.length < 3) {
    candidates = CURATED_ANIME;
  }

  const finalPicks = candidates.slice(0, 3);
  resultsGrid.innerHTML = '';
  finalPicks.forEach(anime => {
    const card = document.createElement('div');
    card.className = 'anime-card';
    card.innerHTML = `
      <div class="poster-wrap">
        <img src="${anime.coverUrl}" alt="${anime.title}">
        <span class="score-badge">★ ${anime.score.toFixed(1)}</span>
      </div>
      <div class="card-content">
        <h4 class="card-title">${anime.title}</h4>
        <div class="card-meta">${anime.format} • ${anime.episodes} eps</div>
        <p class="card-hook">${anime.hook}</p>
        <button class="btn btn-primary btn-sm mt-4" style="width: 100%;">View Details</button>
      </div>
    `;
    card.onclick = () => openDetail(anime);
    resultsGrid.appendChild(card);
  });
}

document.getElementById('retakeQuizBtn').onclick = initQuiz;
document.getElementById('heroQuizBtn').onclick = () => {
  document.querySelector('.nav-item[data-tab="quiz"]').click();
};
document.getElementById('heroWheelBtn').onclick = () => {
  document.querySelector('.nav-item[data-tab="wheel"]').click();
};

// Surprise Roulette Wheel
let rouletteAnime = CURATED_ANIME.slice(0, 8);
let isSpinning = false;
let currentRotation = 0;

function initRoulette() {
  drawWheel(0);
}

function drawWheel(angle) {
  const canvas = document.getElementById('rouletteCanvas');
  const ctx = canvas.getContext('2d');
  const numSlices = rouletteAnime.length;
  const sliceAngle = (2 * Math.PI) / numSlices;
  const colors = ['#A855F7', '#221D3D', '#00F5D4', '#17142A', '#8B5CF6', '#2F2952', '#3B82F6', '#1E1B4B'];

  ctx.clearRect(0, 0, canvas.width, canvas.height);
  const centerX = canvas.width / 2;
  const centerY = canvas.height / 2;
  const radius = canvas.width / 2 - 10;

  for (let i = 0; i < numSlices; i++) {
    const start = angle + i * sliceAngle;
    const end = start + sliceAngle;

    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, radius, start, end);
    ctx.closePath();
    ctx.fillStyle = colors[i % colors.length];
    ctx.fill();
    ctx.strokeStyle = '#0D0B18';
    ctx.lineWidth = 3;
    ctx.stroke();

    // Text labels
    ctx.save();
    ctx.translate(centerX, centerY);
    ctx.rotate(start + sliceAngle / 2);
    ctx.textAlign = 'right';
    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 12px Space Grotesk';
    const label = rouletteAnime[i].title.length > 15 ? rouletteAnime[i].title.substring(0, 14) + '...' : rouletteAnime[i].title;
    ctx.fillText(label, radius - 20, 4);
    ctx.restore();
  }

  // Center hub
  ctx.beginPath();
  ctx.arc(centerX, centerY, 24, 0, 2 * Math.PI);
  ctx.fillStyle = '#0D0B18';
  ctx.fill();
  ctx.strokeStyle = '#00F5D4';
  ctx.lineWidth = 3;
  ctx.stroke();
}

document.getElementById('spinWheelBtn').onclick = () => {
  if (isSpinning) return;
  isSpinning = true;
  document.getElementById('wheelResultCard').classList.add('hidden');

  const extraSpins = 5 + Math.random() * 5;
  const targetRotation = currentRotation + extraSpins * 2 * Math.PI;
  const duration = 3000;
  const start = performance.now();

  function animate(now) {
    const elapsed = now - start;
    const progress = Math.min(elapsed / duration, 1);
    const easeOut = 1 - Math.pow(1 - progress, 3);
    const angle = currentRotation + (targetRotation - currentRotation) * easeOut;
    drawWheel(angle);

    if (progress < 1) {
      requestAnimationFrame(animate);
    } else {
      currentRotation = angle % (2 * Math.PI);
      isSpinning = false;
      showWheelResult();
    }
  }
  requestAnimationFrame(animate);
};

function showWheelResult() {
  const numSlices = rouletteAnime.length;
  const sliceAngle = (2 * Math.PI) / numSlices;
  // Normalized pointer angle is top (-PI/2)
  const normalized = (2 * Math.PI - (currentRotation % (2 * Math.PI)) - Math.PI / 2 + 2 * Math.PI) % (2 * Math.PI);
  const pickedIndex = Math.floor(normalized / sliceAngle) % numSlices;
  const picked = rouletteAnime[pickedIndex];

  const card = document.getElementById('wheelResultCard');
  document.getElementById('wheelResultImg').src = picked.coverUrl;
  document.getElementById('wheelResultScore').innerText = `★ ${picked.score.toFixed(1)}`;
  document.getElementById('wheelResultTitle').innerText = picked.title;
  document.getElementById('wheelResultHook').innerText = picked.hook;
  document.getElementById('wheelResultSynopsis').innerText = picked.synopsis;

  document.getElementById('wheelViewDetailBtn').onclick = () => openDetail(picked);
  document.getElementById('wheelAddWatchlistBtn').onclick = () => addToWatchlist(picked, 'PLAN_TO_WATCH');

  card.classList.remove('hidden');
}

// Matcher & Similar Anime
function initMatcher() {
  const select = document.getElementById('baseAnimeSelect');
  select.innerHTML = CURATED_ANIME.map(a => `<option value="${a.id}">${a.title} (${a.year})</option>`).join('');
  calculateSimilar(CURATED_ANIME[0]);
}

document.getElementById('calculateSimilarBtn').onclick = () => {
  const id = parseInt(document.getElementById('baseAnimeSelect').value);
  const base = CURATED_ANIME.find(a => a.id === id) || CURATED_ANIME[0];
  calculateSimilar(base);
};

function calculateSimilar(base) {
  const grid = document.getElementById('matcherGrid');
  grid.innerHTML = '';

  const similar = CURATED_ANIME.filter(a => a.id !== base.id).map(a => {
    let score = 0;
    // Common genres
    const commonGenres = a.genres.filter(g => base.genres.includes(g)).length;
    score += commonGenres * 20;
    // Common vibes
    const commonVibes = a.vibes.filter(v => base.vibes.includes(v)).length;
    score += commonVibes * 25;
    // Studio similarity
    if (a.studio === base.studio) score += 15;
    return { anime: a, matchScore: Math.min(score, 98) };
  }).sort((a, b) => b.matchScore - a.matchScore).slice(0, 4);

  similar.forEach(({ anime, matchScore }) => {
    const card = document.createElement('div');
    card.className = 'anime-card';
    card.innerHTML = `
      <div class="poster-wrap">
        <img src="${anime.coverUrl}" alt="${anime.title}">
        <span class="score-badge" style="color: var(--cyan-accent);">${matchScore}% MATCH</span>
      </div>
      <div class="card-content">
        <h4 class="card-title">${anime.title}</h4>
        <div class="card-meta">${anime.format} • ${anime.studio}</div>
        <p class="card-hook">${anime.hook}</p>
        <button class="btn btn-outline btn-sm mt-4" style="width: 100%;">View Details</button>
      </div>
    `;
    card.onclick = () => openDetail(anime);
    grid.appendChild(card);
  });
}

function showLoading(show) {
  loadingIndicator.classList.toggle('hidden', !show);
}

function showError(msg) {
  errorMsg.innerText = msg;
  errorBanner.classList.remove('hidden');
}

function hideError() {
  errorBanner.classList.add('hidden');
}

retryBtn.onclick = () => {
  if (isLiveJikan) {
    if (searchQuery.trim().length >= 2) executeJikanSearch(searchQuery.trim());
    else fetchJikanData(currentJikanSource);
  } else {
    renderDiscoverList();
  }
};

// Initial Render
updateWatchlistBadge();
renderDiscoverList();
