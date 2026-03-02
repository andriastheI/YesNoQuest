package com.dre.yesnoquest.service

import com.dre.yesnoquest.model.Theme
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class HtmlBuilder {

    fun buildFilename(theme: Theme): String {
        val ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        return "yesno_${theme.name.lowercase()}_$ts.html"
    }

    fun buildHtml(question: String, theme: Theme): String {
        escapeHtml(question.trim().ifBlank { "Will you say YES?" })
        themeCss(theme)
        val safeQuestion = escapeHtml(question.trim())

        return """
            <!doctype html>
            <html>
            <head>
              <meta charset="utf-8"/>
              <meta name="viewport" content="width=device-width,initial-scale=1"/>
              <title>Yes / No Quest</title>
              <style>
                :root{
                  --bg:#070a12;
                  --card:#0e1224;
                  --border:rgba(255,255,255,.10);
                  --text:#e5e7eb;
                  --muted:#9ca3af;
                  --yes:#22c55e;
                  --no:#ef4444;
                }
            
                body{
                  margin:0;
                  min-height:100vh;
                  display:flex;
                  align-items:center;
                  justify-content:center;
                  background:radial-gradient(1200px 700px at 50% 20%, rgba(59,130,246,.18), transparent 60%),
                             radial-gradient(900px 500px at 30% 60%, rgba(168,85,247,.14), transparent 55%),
                             var(--bg);
                  color:var(--text);
                  font-family:system-ui,-apple-system,Segoe UI,Roboto,Arial;
                  padding:24px;
                }
            
                .card{
                  width:min(820px, 100%);
                  border:1px solid var(--border);
                  background:linear-gradient(180deg, rgba(255,255,255,.05), rgba(255,255,255,.02));
                  border-radius:22px;
                  padding:28px;
                  box-shadow:0 20px 80px rgba(0,0,0,.55);
                  position:relative;
                  overflow:hidden;
                }
            
                .tag{
                  display:inline-flex;
                  align-items:center;
                  gap:10px;
                  font-size:12px;
                  letter-spacing:.2em;
                  color:var(--muted);
                  border:1px solid var(--border);
                  padding:8px 12px;
                  border-radius:999px;
                  text-transform:uppercase;
                  width:fit-content;
                }
            
                h1{margin:18px 0 8px;font-size:44px;line-height:1.05;}
                .q{font-size:20px;color:var(--text);opacity:.9;margin-bottom:20px;}
            
                /* This is the playground where the NO button can run around */
                .arena{
                  position:relative;           /* ✅ important: NO is positioned relative to this */
                  height:180px;                /* ✅ gives space to run */
                  border:1px dashed rgba(255,255,255,.12);
                  border-radius:16px;
                  margin-top:18px;
                  overflow:hidden;             /* ✅ keeps NO inside */
                  background:rgba(0,0,0,.18);
                }
            
                .btnRow{
                  display:flex;
                  gap:14px;
                  align-items:center;
                  padding:18px;
                }
            
                button{
                  font-weight:900;
                  letter-spacing:.08em;
                  border:none;
                  border-radius:14px;
                  padding:14px 20px;
                  cursor:pointer;
                  user-select:none;
                  color:#0b0b0c;
                }
            
                #yesBtn{ background:var(--yes); }
                #noBtn{
                  background:var(--no);
                  position:absolute;            /* ✅ we will move it */
                  left:220px;                   /* ✅ starts visible */
                  top:74px;
                  z-index:10;                   /* ✅ always on top */
                  pointer-events:auto;          /* ✅ still detects hover/mouse proximity */
                }
            
                .status{
                  margin-top:14px;
                  color:var(--muted);
                  font-size:13px;
                  display:flex;
                  align-items:center;
                  gap:10px;
                }
            
                .msg{
                  margin-top:12px;
                  padding:12px 14px;
                  border:1px solid var(--border);
                  border-radius:14px;
                  background:rgba(0,0,0,.2);
                  font-size:14px;
                  color:var(--text);
                  min-height:22px;
                }
            
                .foot{margin-top:10px;font-size:12px;color:var(--muted);opacity:.9;}
              </style>
            </head>
            <body>
              <div class="card">
                <div class="tag">YES / NO QUEST</div>
            
                <h1>Answer this.</h1>
                <div class="q" id="questionText">$safeQuestion</div>
            
                <div class="arena" id="arena">
                  <div class="btnRow">
                    <button id="yesBtn" type="button">YES</button>
                  </div>
            
                  <!-- ✅ NO starts visible, then runs away -->
                  <button id="noBtn" type="button" aria-disabled="true" title="Nope 😈">NO</button>
                </div>
            
                <div class="status" id="lockRow" style="display:none;">Decision locked in ✅</div>
                <div class="msg" id="resultBox" style="display:none;"></div>
                <div class="foot">Local-only. Nothing is sent anywhere.</div>
              </div>
            
              <script>
                const arena = document.getElementById("arena");
                const noBtn = document.getElementById("noBtn");
                const yesBtn = document.getElementById("yesBtn");
                const lockRow = document.getElementById("lockRow");
                const resultBox = document.getElementById("resultBox");
            
                let locked = false;
            
                // ✅ Make NO "not work" even if they click it
                noBtn.addEventListener("click", (e) => {
                  e.preventDefault();
                  e.stopPropagation();
                  // do nothing on purpose 😈
                });
            
                yesBtn.addEventListener("click", () => {
                  if (locked) return;
                  locked = true;
                  lockRow.style.display = "flex";
                  resultBox.style.display = "block";
                  resultBox.textContent = "You chose YES. Wise choice 😌";
                });
            
                function clamp(v, min, max){ return Math.max(min, Math.min(max, v)); }
            
                function moveNoButtonAwayFrom(mouseX, mouseY) {
                  const a = arena.getBoundingClientRect();
            
                  // Current NO center
                  const b = noBtn.getBoundingClientRect();
                  const noCenterX = b.left + b.width / 2;
                  const noCenterY = b.top + b.height / 2;
            
                  // Vector from mouse -> NO
                  let dx = noCenterX - mouseX;
                  let dy = noCenterY - mouseY;
                  const dist = Math.hypot(dx, dy) || 1;
            
                  // Push strength
                  const push = 120; // how far it jumps each time
            
                  dx = (dx / dist) * push;
                  dy = (dy / dist) * push;
            
                  // Convert to arena-local coordinates
                  const targetLeft = (b.left - a.left) + dx;
                  const targetTop  = (b.top  - a.top ) + dy;
            
                  // Keep inside arena bounds
                  const maxLeft = a.width - b.width - 8;
                  const maxTop  = a.height - b.height - 8;
            
                  const newLeft = clamp(targetLeft, 8, maxLeft);
                  const newTop  = clamp(targetTop, 8, maxTop);
            
                  noBtn.style.left = newLeft + "px";
                  noBtn.style.top  = newTop + "px";
                }
            
                // ✅ Run away when cursor gets close (not only on hover)
                arena.addEventListener("mousemove", (e) => {
                  if (locked) return;
            
                  const b = noBtn.getBoundingClientRect();
                  const cx = b.left + b.width / 2;
                  const cy = b.top + b.height / 2;
            
                  const dist = Math.hypot(e.clientX - cx, e.clientY - cy);
            
                  // If cursor within this radius, NO runs
                  const dangerRadius = 140;
            
                  if (dist < dangerRadius) {
                    moveNoButtonAwayFrom(e.clientX, e.clientY);
                  }
                });
            
                // Extra: if they manage to hover NO, it instantly moves
                noBtn.addEventListener("mouseenter", (e) => {
                  if (locked) return;
                  moveNoButtonAwayFrom(e.clientX, e.clientY);
                });
              </script>
            </body>
            </html>
        """.trimIndent()
    }

    private fun themeCss(theme: Theme): String {
        val vars = when (theme) {
            Theme.NEON -> ":root{--bg:#070814;--card:#0f1026;--text:#e9e9ff;--muted:#a7a7d6;--yes:#00f5d4;--no:#ff4d6d;--accent:#7b2cff;}"
            Theme.DARK -> ":root{--bg:#0b0b0c;--card:#141416;--text:#f3f4f6;--muted:#9ca3af;--yes:#34d399;--no:#f87171;--accent:#60a5fa;}"
            Theme.MINIMAL -> ":root{--bg:#f5f6f8;--card:#ffffff;--text:#111827;--muted:#6b7280;--yes:#16a34a;--no:#dc2626;--accent:#2563eb;}"
            Theme.RETRO -> ":root{--bg:#0b1020;--card:#111a33;--text:#fef08a;--muted:#93c5fd;--yes:#22c55e;--no:#fb7185;--accent:#f97316;}"
        }

        return """
          $vars
          *{box-sizing:border-box}
          body{
            margin:0; min-height:100vh; display:flex; align-items:center; justify-content:center;
            background:var(--bg); color:var(--text); font-family:system-ui,-apple-system,Segoe UI,Roboto,Arial,sans-serif;
            padding:24px; overflow:hidden;
          }
          .card{
            width:min(760px, 100%);
            background:var(--card);
            border:1px solid rgba(255,255,255,.12);
            border-radius:20px;
            padding:28px;
            box-shadow:0 18px 50px rgba(0,0,0,.35);
            position:relative;
          }
          .badge{
            display:inline-block; padding:8px 12px; border-radius:999px;
            border:1px solid rgba(255,255,255,.14); color:var(--muted);
            letter-spacing:.12em; font-size:12px; text-transform:uppercase;
          }
          .title{margin:16px 0 8px; font-size:34px; line-height:1.1;}
          .question{margin:0 0 14px; font-size:20px; opacity:.95;}
          .stage{
            position:relative; height:120px;
            display:flex; align-items:center; gap:14px;
          }
          .btn{
            border:none; cursor:pointer; border-radius:14px; padding:14px 18px;
            font-weight:800; font-size:16px;
            transition:transform .08s ease, filter .15s ease;
            user-select:none;
          }
          .btn:active{ transform:scale(.98); }
          .btn.yes{ background:color-mix(in srgb, var(--yes) 30%, transparent); color:var(--text); border:1px solid color-mix(in srgb, var(--yes) 70%, transparent); }
          .btn.no{  background:color-mix(in srgb, var(--no)  30%, transparent); color:var(--text); border:1px solid color-mix(in srgb, var(--no)  70%, transparent); }
          .status{margin-top:6px; color:var(--muted); font-size:14px;}
          .result{margin-top:14px; padding:14px 16px; border-radius:14px; border:1px dashed rgba(255,255,255,.18); min-height:22px;}
          .footer{margin-top:16px; font-size:12px; color:var(--muted);}
        """.trimIndent()
    }

    private fun escapeHtml(s: String): String =
        s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
}