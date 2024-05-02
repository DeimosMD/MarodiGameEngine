package org.deimoscm.gui

import org.deimoscm.*
import java.awt.*
import javax.swing.*

class Home : JPanel(BorderLayout()) {

    private val buildTasks: Array<String> = arrayOf(
        "Run in panel below",
        "Run in new window",
        "Build jar"
    )
    private val executeTaskButton: JButton = JButton("Execute Build Task")
    private val selectWkDirButton: JButton = JButton("Select Working Directory")
    private val generateProjectStructureButton: JButton = JButton("Generate Project Structure")
    private val selectBuildTaskDropdown: JComboBox<String> = JComboBox(buildTasks)
    private val topBar: JPanel = JPanel(BorderLayout())
    private val wkDirSection = JPanel(FlowLayout())
    private val buildTaskSection = JPanel()
    var gameGraphicsPanelSection: JPanel = JPanel()
    private val wkDirLabel: JLabel = JLabel(wkDir)
    private val leftContentLayout = SpringLayout()
    private val leftBar: JPanel = JPanel(BorderLayout())
    private val leftContentSection: JPanel = JPanel(leftContentLayout)
    private val leftSeparatorSection: JPanel = JPanel()
    private val darkGrey1: Color = Color(leftContentSection.background.red-30, leftContentSection.background.blue-15, leftContentSection.background.green-15)
    private val mainClassTextField = JTextField()
    private val jarNameTextField = JTextField()
    private val gameClassTextField = JTextField()
    private val headerFont = Font(Font.MONOSPACED, Font.PLAIN, 20)
    private val textFieldSize =  Dimension(180, 30)
    private val textFieldSectionSize = Dimension (300, 40)
    private val mainClassSection = JPanel()
    private val jarNameSection = JPanel()
    private val gameClassSection = JPanel()
    private val mainClassLabel = JLabel("Main class")
    private val jarNameLabel = JLabel("Jar name")
    private val gameClassLabel = JLabel("Game class")
    private val saveBuildSettingsButton = JButton("Save Build Settings")
    val taskRunningLabel = JLabel("Task Running...")
    private val useCustomLibPathCheckBox: JCheckBox = JCheckBox()
    private val useCustomLibPathSection = JPanel()
    private val useCustomLibPathLabel = JLabel("Use custom lib path?")
    private val customLibPathTextField = JTextField().also {it.isEnabled = false}
    private val saveEngineSettingsButton = JButton("Save Engine Settings")

    init {
        this.add(topBar, BorderLayout.NORTH)
        this.add(gameGraphicsPanelSection, BorderLayout.CENTER)
        this.add(leftBar, BorderLayout.WEST)
        // top bar
        topBar.add(wkDirSection, BorderLayout.WEST)
        topBar.add(buildTaskSection, BorderLayout.EAST)
        buildTaskSection.add(selectBuildTaskDropdown)
        buildTaskSection.add(executeTaskButton)
        buildTaskSection.add(taskRunningLabel)
        taskRunningLabel.isVisible = false
        wkDirSection.add(wkDirLabel)
        // left bar
        leftBar.add(leftContentSection, BorderLayout.EAST)
        leftBar.add(leftSeparatorSection, BorderLayout.WEST)
        leftSeparatorSection.add(Box.createHorizontalStrut(15))
        leftContentSection.preferredSize = Dimension(300, 0)
        leftContentSection.background = darkGrey1
        leftSeparatorSection.background = darkGrey1
        jarNameSection.background = darkGrey1
        mainClassSection.background = darkGrey1
        gameClassSection.background = darkGrey1
        useCustomLibPathSection.background = darkGrey1
        // project management section
        leftContentSection.add(JLabel("Project Management").also {
            it.font = headerFont
            leftContentLayout.putConstraint(SpringLayout.NORTH, it, 30, SpringLayout.NORTH, leftContentSection)
            leftContentLayout.putConstraint(SpringLayout.NORTH, selectWkDirButton, 20, SpringLayout.SOUTH, it)
        })
        leftContentSection.add(selectWkDirButton)
        leftContentSection.add(generateProjectStructureButton)
        leftContentLayout.putConstraint(SpringLayout.NORTH, generateProjectStructureButton, 20, SpringLayout.SOUTH, selectWkDirButton)
        // build settings section
        leftContentSection.add(JLabel("Build Settings").also {
            it.font = headerFont
            leftContentLayout.putConstraint(SpringLayout.NORTH, mainClassSection, 20, SpringLayout.SOUTH, it)
            leftContentLayout.putConstraint(SpringLayout.NORTH, it, 50, SpringLayout.SOUTH, generateProjectStructureButton)
        })
        leftContentSection.add(mainClassSection)
        leftContentLayout.putConstraint(SpringLayout.NORTH, gameClassSection, 20, SpringLayout.SOUTH, mainClassSection)
        leftContentSection.add(gameClassSection)
        leftContentLayout.putConstraint(SpringLayout.NORTH, jarNameSection, 20, SpringLayout.SOUTH, gameClassSection)
        leftContentSection.add(jarNameSection)
        leftContentLayout.putConstraint(SpringLayout.NORTH, saveBuildSettingsButton, 20, SpringLayout.SOUTH, jarNameSection)
        leftContentSection.add(saveBuildSettingsButton)
        leftContentLayout.putConstraint(SpringLayout.EAST, mainClassSection, -5, SpringLayout.EAST, leftContentSection)
        leftContentLayout.putConstraint(SpringLayout.EAST, jarNameSection, -5, SpringLayout.EAST, leftContentSection)
        leftContentLayout.putConstraint(SpringLayout.EAST, gameClassSection, -5, SpringLayout.EAST, leftContentSection)
        mainClassTextField.preferredSize = textFieldSize
        jarNameTextField.preferredSize = textFieldSize
        gameClassTextField.preferredSize = textFieldSize
        mainClassSection.maximumSize = textFieldSectionSize
        jarNameSection.maximumSize = textFieldSectionSize
        gameClassSection.maximumSize = textFieldSectionSize
        mainClassSection.add(mainClassLabel)
        mainClassSection.add(mainClassTextField)
        jarNameSection.add(jarNameLabel)
        jarNameSection.add(jarNameTextField)
        gameClassSection.add(gameClassLabel)
        gameClassSection.add(gameClassTextField)
        // engine settings section
        leftContentSection.add(JLabel("Engine Settings").also {
            it.font = headerFont
            leftContentLayout.putConstraint(SpringLayout.NORTH, it, 50, SpringLayout.SOUTH, saveBuildSettingsButton)
            leftContentLayout.putConstraint(SpringLayout.NORTH, useCustomLibPathSection, 20, SpringLayout.SOUTH, it)
        })
        leftContentSection.add(useCustomLibPathSection)
        leftContentSection.add(customLibPathTextField)
        leftContentLayout.putConstraint(SpringLayout.NORTH, customLibPathTextField, 10, SpringLayout.SOUTH, useCustomLibPathSection)
        leftContentSection.add(saveEngineSettingsButton)
        leftContentLayout.putConstraint(SpringLayout.NORTH, saveEngineSettingsButton, 20, SpringLayout.SOUTH, customLibPathTextField)
        useCustomLibPathSection.add(useCustomLibPathLabel)
        useCustomLibPathSection.add(useCustomLibPathCheckBox)
        customLibPathTextField.preferredSize = Dimension(250, 30)

        gameGraphicsPanelSection.background = Color.BLACK
        val buildTaskHandler = BuildTaskHandler(selectBuildTaskDropdown, this)
        executeTaskButton.addActionListener(buildTaskHandler)

        selectWkDirButton.addActionListener {
            val fileChooser = JFileChooser(wkDir)
            fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                wkDir = fileChooser.selectedFile.absolutePath
                wkDirLabel.text = wkDir
                individualProjectSettings = IndividualProjectSettings()
                individualProjectSettings.readFromConfigFile()
                jarNameTextField.text = individualProjectSettings.jarName
                mainClassTextField.text = individualProjectSettings.mainClass
                gameClassTextField.text = individualProjectSettings.gameClass
            }
        }

        generateProjectStructureButton.addActionListener {
            val confirmationFrame = JFrame("Confirmation Window")
            val confirmButton = JButton("Confirm")
            val cancelButton = JButton("Cancel")
            val optionsPanel = JPanel(FlowLayout(FlowLayout.CENTER, 50, 0))
            optionsPanel.add(cancelButton)
            optionsPanel.add(confirmButton)
            confirmationFrame.layout = BorderLayout()
            confirmationFrame.add(JLabel("This operation will generate/update a project at '$wkDir'."), BorderLayout.NORTH)
            confirmationFrame.add(optionsPanel, BorderLayout.SOUTH)
            confirmationFrame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            confirmationFrame.pack()
            confirmationFrame.isResizable = false
            cancelButton.addActionListener {confirmationFrame.dispose()}
            confirmButton.addActionListener {confirmationFrame.dispose(); buildTaskHandler.generateProjectStructure()}
            confirmationFrame.setLocationRelativeTo(this)
            confirmationFrame.isVisible = true
        }

        saveBuildSettingsButton.addActionListener {
            individualProjectSettings.jarName = jarNameTextField.text
            individualProjectSettings.mainClass = mainClassTextField.text
            individualProjectSettings.gameClass = gameClassTextField.text
            individualProjectSettings.writeToConfigFile()
        }

        saveEngineSettingsButton.addActionListener {
            engineSettings.writeToConfigFile()
        }
    }
}
