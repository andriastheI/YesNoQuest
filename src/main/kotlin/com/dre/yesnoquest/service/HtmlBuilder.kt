/**
 * Filename: HtmlBuilder.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Generates themed Yes/No HTML content for the YesNoQuest application.
 *
 * This service is responsible for:
 * - Creating unique HTML filenames for email attachments
 * - Building a complete standalone HTML document as a string
 * - Applying a selected visual Theme
 * - Escaping user-provided input to prevent HTML injection
 *
 * Output Notes:
 * - The generated HTML is meant to be downloaded and opened locally
 * - No network requests are made by the HTML
 * - JavaScript logic is embedded directly in the document
 *
 * Security Notes:
 * - User input is escaped via escapeHtml(...) before insertion into the HTML
 * - The output is intended to run locally on the recipient's machine
 */

package com.dre.yesnoquest.service

import com.dre.yesnoquest.model.Theme
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Service that builds themed HTML documents for the YesNoQuest email attachment.
 */
@Service
class HtmlBuilder {

    /**
     * Builds a unique filename for the generated HTML attachment.
     *
     * Format:
     *   yesno_<theme>_<yyyyMMdd_HHmmss>.html
     *
     * Example:
     *   yesno_neon_20260303_142522.html
     *
     * @param theme The selected visual theme
     * @return A unique filename suitable for attachments
     */
    fun buildFilename(theme: Theme): String {
        val ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        return "yesno_${theme.name.lowercase()}_$ts.html"
    }

    /**
     * Builds the full HTML document (doctype → html → head → style → body → script).
     *
     * Behavior included in output HTML:
     * - Displays the provided question text
     * - Shows a YES button that locks the decision and displays a message
     * - Shows a NO button that runs away when the cursor gets near
     * - Prevents NO from being clicked (click handler cancels events)
     *
     * Input Handling:
     * - The question is trimmed
     * - If blank, a default prompt is used
     * - Question is escaped to prevent injection
     *
     * Theme Handling:
     * - A theme-specific CSS string is selected using themeCss(theme)
     * - NOTE: In the current implementation, themeCss(theme) is called but its
     *   returned value is not injected into the <style> block. The page currently
     *   uses the base CSS defined directly inside the template string.
     *
     * @param question User-provided question text
     * @param theme Selected theme enum
     * @return Complete HTML document as a string
     */
    fun buildHtml(question: String, theme: Theme): String {

        // ✅ Default + escape (this actually gets used now)
        val safeQuestion = escapeHtml(question.trim().ifBlank { "Will you say YES?" })

        // ✅ Theme CSS variables (injected into <style>)
        val themeVars = themeCss(theme)

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
        
                    /* ✅ Theme overrides AFTER defaults so they win */
                    $themeVars
        
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
        
                    .arena{
                      position:relative;
                      height:180px;
                      border:1px dashed rgba(255,255,255,.12);
                      border-radius:16px;
                      margin-top:18px;
                      overflow:hidden;
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
                      position:absolute;
                      left:220px;
                      top:74px;
                      z-index:10;
                      pointer-events:auto;
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
        
                    noBtn.addEventListener("click", (e) => {
                      e.preventDefault();
                      e.stopPropagation();
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
                      const b = noBtn.getBoundingClientRect();
        
                      const noCenterX = b.left + b.width / 2;
                      const noCenterY = b.top + b.height / 2;
        
                      let dx = noCenterX - mouseX;
                      let dy = noCenterY - mouseY;
                      const dist = Math.hypot(dx, dy) || 1;
        
                      const push = 120;
        
                      dx = (dx / dist) * push;
                      dy = (dy / dist) * push;
        
                      const targetLeft = (b.left - a.left) + dx;
                      const targetTop  = (b.top  - a.top ) + dy;
        
                      const maxLeft = a.width - b.width - 8;
                      const maxTop  = a.height - b.height - 8;
        
                      const newLeft = clamp(targetLeft, 8, maxLeft);
                      const newTop  = clamp(targetTop, 8, maxTop);
        
                      noBtn.style.left = newLeft + "px";
                      noBtn.style.top  = newTop + "px";
                    }
        
                    arena.addEventListener("mousemove", (e) => {
                      if (locked) return;
        
                      const b = noBtn.getBoundingClientRect();
                      const cx = b.left + b.width / 2;
                      const cy = b.top + b.height / 2;
        
                      const dist = Math.hypot(e.clientX - cx, e.clientY - cy);
                      const dangerRadius = 140;
        
                      if (dist < dangerRadius) {
                        moveNoButtonAwayFrom(e.clientX, e.clientY);
                      }
                    });
        
                    noBtn.addEventListener("mouseenter", (e) => {
                      if (locked) return;
                      moveNoButtonAwayFrom(e.clientX, e.clientY);
                    });
                  </script>
                </body>
                </html>
            """.trimIndent()
    }

    /**
     * Returns CSS variables and base styling for the selected Theme.
     *
     * This method is designed to support multiple distinct themes by
     * switching CSS variables (colors, accents, etc.).
     *
     * NOTE:
     * - The current buildHtml(...) function does not inject this returned
     *   CSS into the final HTML output. It is currently unused by the template.
     *
     * @param theme Selected Theme enum
     * @return Theme-specific CSS block as a string
     */
    private fun themeCss(theme: Theme): String {
        val vars = when (theme) {
            Theme.NEON ->
                ":root{--bg:#070814;--card:#0f1026;--text:#e9e9ff;--muted:#a7a7d6;--yes:#00f5d4;--no:#ff4d6d;--border:rgba(255,255,255,.14);}"
            Theme.DARK ->
                ":root{--bg:#0b0b0c;--card:#141416;--text:#f3f4f6;--muted:#9ca3af;--yes:#34d399;--no:#f87171;--border:rgba(255,255,255,.10);}"
            Theme.MINIMAL ->
                ":root{--bg:#f5f6f8;--card:#ffffff;--text:#111827;--muted:#6b7280;--yes:#16a34a;--no:#dc2626;--border:rgba(17,24,39,.14);}"
            Theme.RETRO ->
                ":root{--bg:#0b1020;--card:#111a33;--text:#fef08a;--muted:#93c5fd;--yes:#22c55e;--no:#fb7185;--border:rgba(255,255,255,.16);}"
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

    /**
     * Escapes potentially dangerous HTML characters.
     *
     * Prevents HTML injection by converting special characters into
     * safe HTML entities.
     *
     * @param s Raw user-provided string
     * @return Escaped safe string suitable for HTML insertion
     */
    private fun escapeHtml(s: String): String =
        s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
}